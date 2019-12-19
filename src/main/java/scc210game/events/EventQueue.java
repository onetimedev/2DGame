package scc210game.events;

import scc210game.ecs.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * An event queue that allows events to be distributed to listeners.
 *
 * This is a singleton class, use like: `EventQueue.listen`, etc.
 */
public class EventQueue {
    @Nullable
    private static EventQueue instance = null;

    @Nonnull
    private final HashMap<Entity, ArrayDeque<Event>> queues;
    @Nonnull
    private final HashMap<Class<? extends Event>, HashSet<Entity>> registered;

    public EventQueue() {
        this.queues = new HashMap<>();
        this.registered = new HashMap<>();
    }

    /**
     * Register `e` to listen to events of type `on`
     *
     * @param e  The {@link Entity} that is listening
     * @param on The type of {@link Event} to listen on
     */
    public static void listen(Entity e, Class<? extends Event> on) {
        var instance = getInstance();
        instance.queues.computeIfAbsent(e, k -> new ArrayDeque<>());
        instance.registered.computeIfAbsent(on, k -> new HashSet<>()).add(e);
    }

    /**
     * Unregister `e` from listening to events of type `on`
     *
     * @param e  The {@link Entity} that is listening
     * @param on The type of event to stop listening on
     */
    public static void unListen(Entity e, Class<? extends Event> on) {
        EventQueue instance = getInstance();
        HashSet<Entity> set = instance.registered.get(on);

        if (set == null) {
            return;
        }

        set.remove(e);

        if (set.isEmpty()) {
            instance.queues.remove(e);
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

        HashSet<Entity> listeners = instance.registered.get(evt.getClass());

        if (listeners == null) {
            return;
        }

        for (final Entity entID : listeners) {
            instance.queues.get(entID).add(evt);
        }
    }

    /**
     * Fetch an iterator of events for the listening `e`
     *
     * @param e The entity to get events for
     * @return An iterator of events for this entity
     */
    @Nonnull
    public static Iterator<Event> getEventsFor(Entity e) {
        var q = getInstance().queues.get(e);

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
