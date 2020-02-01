package scc210game.engine.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

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
    private final HashMap<EventQueueReader, ArrayDeque<Event>> queues;
    @Nonnull
    private final HashMap<Class<? extends Event>, HashSet<EventQueueReader>> registered;
    @Nonnull
    private final DelayQueue<DelayedEvent> delayedEvents;

    public EventQueue() {
        this.queues = new HashMap<>();
        this.registered = new HashMap<>();
        this.delayedEvents = new DelayQueue<>();
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
        instance.registered.computeIfAbsent(on, k -> new HashSet<>()).add(r);
    }

    /**
     * Unregister `r` from listening to events of type `on`
     *
     * @param r  The {@link EventQueueReader} that is listening
     * @param on The type of event to stop listening on
     */
    public static void unListen(EventQueueReader r, Class<? extends Event> on) {
        EventQueue instance = getInstance();
        HashSet<EventQueueReader> set = instance.registered.get(on);

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
            HashSet<EventQueueReader> listeners = instance.registered.get(c);

            if (listeners != null) {
                for (final EventQueueReader r : listeners) {
                    instance.queues.get(r).add(evt);
                }
            }

            c = c.getSuperclass();
        }
    }

    /**
     * Broadcast an event to all listeners at the specified Instant.
     *
     * @param evt The event to broadcast
     * @param ins When to broadcast the event
     */
    public static void broadcastAt(@Nonnull Event evt, @Nonnull Instant ins) {
        getInstance().delayedEvents.add(new DelayedEvent(evt, ins));
    }

    /**
     * Broadcast an event to all listeners after the specified Duration.
     *
     * @param evt The event to broadcast
     * @param delay How long to wait before broadcasting the event
     */
    public static void broadcastIn(@Nonnull Event evt, @Nonnull Duration delay) {
        broadcastAt(evt, Instant.now().plus(delay));
    }

    private static void updateDelayedEvents() {
        var inst = getInstance();
        DelayedEvent evt;
        while ((evt = inst.delayedEvents.poll()) != null) {
            broadcast(evt.e);
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
                updateDelayedEvents();
                return !q.isEmpty();
            }

            @Override
            public Event next() {
                updateDelayedEvents();
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

    private static class DelayedEvent implements Delayed {
        public final Event e;
        public final Instant end;


        private DelayedEvent(Event e, Instant end) {
            this.e = e;
            this.end = end;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return Instant.now().until(this.end, unit.toChronoUnit());
        }

        @Override
        public int compareTo(@Nonnull Delayed o) {
            var de = (DelayedEvent) o;
            return this.end.compareTo(de.end);
        }
    }
}
