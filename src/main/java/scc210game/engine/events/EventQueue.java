package scc210game.engine.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * An event queue that allows events to be distributed to listeners.
 * <p>
 * This is a singleton class, use like: `EventQueue.listen`, etc.
 */
public class EventQueue {
    @Nullable
    private static EventQueue instance = null;


    private long lastReaderID = 0;
    @Nonnull
    private final Map<EventQueueReader, ArrayDeque<Event>> queues;
    @Nonnull
    private final Map<Class<? extends Event>, Set<EventQueueReader>> registered;

    public EventQueue() {
        this.queues = new WeakHashMap<>();
        this.registered = new WeakHashMap<>();
    }

    /**
     * Get an event queue reader that can be used to register
     * listeners and collect fired events
     *
     * @return a new {@link EventQueueReader}
     */
    public static EventQueueReader makeReader() {
        return new EventQueueReader(getInstance().lastReaderID++);
    }

    /**
     * Register `r` to listen to events of type `on`
     *
     * @param r  The {@link EventQueueReader} that is listening
     * @param on The type of {@link Event} to listen on
     */
    public static void listen(EventQueueReader r, Class<? extends Event> on) {
        var instance = getInstance();
        instance.queues.computeIfAbsent(r, k -> new ArrayDeque<>());
        instance.registered.computeIfAbsent(on, k -> Collections.newSetFromMap(new WeakHashMap<>())).add(r);
    }

    /**
     * Unregister `r` from listening to events of type `on`
     *
     * @param r  The {@link EventQueueReader} that is listening
     * @param on The type of event to stop listening on
     */
    public static void unListen(EventQueueReader r, Class<? extends Event> on) {
        EventQueue instance = getInstance();
        Set<EventQueueReader> set = instance.registered.get(on);

        if (set == null) {
            return;
        }

        set.remove(r);

        if (set.isEmpty()) {
            instance.queues.remove(r);
            instance.registered.remove(on);
        }
    }

    /**
     * Broadcast an event to all listeners.
     *
     * @param evt The event to broadcast
     */
    public static void broadcast(@Nonnull Event evt) {
        var instance = getInstance();

        Class<?> c = evt.getClass();

        while (c != null) {
            Set<EventQueueReader> listeners = instance.registered.get(c);

            if (listeners != null) {
                for (final EventQueueReader r : listeners) {
                    instance.queues.get(r).add(evt);
                }
            }

            c = c.getSuperclass();
        }
    }

    /**
     * Fetch an iterator of events for the listening `r`
     *
     * @param r The entity to get events for
     * @return An iterator of events for this entity
     */
    @Nonnull
    public static Iterator<Event> getEventsFor(EventQueueReader r) {
        var q = getInstance().queues.get(r);

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return !q.isEmpty();
            }

            @Override
            public Event next() {
                return q.poll();
            }
        };
    }

    @Nonnull
    private static EventQueue getInstance() {
        if (EventQueue.instance == null)
            EventQueue.instance = new EventQueue();

        return EventQueue.instance;
    }
}
