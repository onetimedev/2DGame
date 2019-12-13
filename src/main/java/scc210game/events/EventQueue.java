package scc210game.events;

import org.jetbrains.annotations.NotNull;

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
    private static EventQueue instance = null;

    private final HashMap<Long, ArrayDeque<Event>> queues;
    private final HashMap<Class<? extends Event>, HashSet<Long>> registered;

    public EventQueue() {
        this.queues = new HashMap<>();
        this.registered = new HashMap<>();
    }

    /**
     * Register `entID` to listen to events of type `on`
     * @param entID The entity that is listening
     * @param on The type of event to listen on
     */
    public static void listen(long entID, Class<? extends Event> on) {
        var instance = getInstance();
        instance.queues.computeIfAbsent(entID, k -> new ArrayDeque<>());
        instance.registered.computeIfAbsent(on, k -> new HashSet<>()).add(entID);
    }

    /**
     * Unregister `entID` from listening to events of type `on`
     * @param entID The entity that is listening
     * @param on The type of event to stop listening on
     */
    public static void unListen(long entID, Class<? extends Event> on) {
        EventQueue instance = getInstance();
        var set = instance.registered.get(on);

        if (set == null) {
            return;
        }

        set.remove(entID);

        if (set.isEmpty()) {
            instance.queues.remove(entID) ;
            instance.registered.remove(on);
        }
    }

    /**
     * Broadcast an event to all listeners.
     *
     * @param e The event to broadcast
     */
    public static void broadcast(@NotNull Event e) {
        var instance = getInstance();

        var listeners = instance.registered.get(e.getClass());

        if (listeners == null) {
            return;
        }

        for (var entID : listeners) {
            instance.queues.get(entID).add(e);
        }
    }

    /**
     * Fetch an iterator of events for the listening `entID`
     * @param entID The entity to get events for
     * @return An iterator of events for this entity
     */
    public static Iterator<Event> getEventsFor(long entID) {
        var q = getInstance().queues.get(entID);
        
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

    private static EventQueue getInstance() {
        if (EventQueue.instance == null)
            EventQueue.instance = new EventQueue();

        return instance;
    }
}
