package scc210game.engine.events;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * An event queue that allows events to be distributed to listeners.
 */
public class EventQueue {
    private static long lastReaderID = 0;
    @Nonnull
    private final Map<EventQueueReader, ArrayDeque<Event>> queues;
    @Nonnull
    private final DelayQueue<DelayedEvent> delayedEvents;

    @Nonnull
    private static final Map<Class<? extends Event>, Set<EventQueueReader>> registered = new HashMap<>();
    @Nonnull
    private static final Set<EventQueue> instances = Collections.newSetFromMap(
            new WeakHashMap<>());

    public EventQueue() {
        this.queues = new HashMap<>() {{
            registered.forEach((k, v) ->
                    v.forEach((r) ->
                            this.put(r, new ArrayDeque<>())));
        }};
        this.delayedEvents = new DelayQueue<>();
        instances.add(this);
    }

    /**
     * Get an event queue reader that can be used to register
     * listeners and collect fired events
     * <p>
     * NOTE: event queue readers are usable on all instances of EventQueue
     * such that registering a reader on one event queue makes it usable on any other event queue
     *
     * @return a new {@link EventQueueReader}
     */
    public EventQueueReader makeReader() {
        return new EventQueueReader(lastReaderID++);
    }

    /**
     * Register `r` to listen to events of type `on`
     *
     * @param r  The {@link EventQueueReader} that is listening
     * @param on The type of {@link Event} to listen on
     */
    public void listen(EventQueueReader r, Class<? extends Event> on) {
        for (final var q : instances) {
            q.queues.computeIfAbsent(r, k -> new ArrayDeque<>());
        }
        registered.computeIfAbsent(on, k -> Collections.newSetFromMap(new WeakHashMap<>())).add(r);
    }

    /**
     * Unregister `r` from listening to events of type `on`
     *
     * @param r  The {@link EventQueueReader} that is listening
     * @param on The type of event to stop listening on
     */
    public void unListen(EventQueueReader r, Class<? extends Event> on) {
        Set<EventQueueReader> set = registered.get(on);

        if (set == null) {
            return;
        }

        set.remove(r);

        if (set.isEmpty()) {
            for (final var q : instances) {
                q.queues.remove(r);
            }
            registered.remove(on);
        }
    }

    /**
     * Broadcast an event to all listeners.
     *
     * @param evt The event to broadcast
     */
    public void broadcast(@Nonnull Event evt) {
        Class<?> c = evt.getClass();

        while (c != null) {
            Set<EventQueueReader> listeners = registered.get(c);

            if (listeners != null) {
                for (final EventQueueReader r : listeners) {
                    this.queues.get(r).add(evt);
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
    public void broadcastAt(@Nonnull Event evt, @Nonnull Instant ins) {
        this.delayedEvents.add(new DelayedEvent(evt, ins));
    }

    /**
     * Broadcast an event to all listeners after the specified Duration.
     *
     * @param evt   The event to broadcast
     * @param delay How long to wait before broadcasting the event
     */
    public void broadcastIn(@Nonnull Event evt, @Nonnull Duration delay) {
        this.broadcastAt(evt, Instant.now().plus(delay));
    }

    private void updateDelayedEvents() {
        DelayedEvent evt;
        while ((evt = this.delayedEvents.poll()) != null) {
            this.broadcast(evt.e);
        }
    }

    public void patchDelayDelta(Duration td) {
        for (final var evt : this.delayedEvents) {
            evt.end = evt.end.plus(td);
        }
    }

    /**
     * Fetch an iterator of events for the listening `r`
     *
     * @param r The entity to get events for
     * @return An iterator of events for this entity
     */
    @Nonnull
    public Iterator<Event> getEventsFor(EventQueueReader r) {
        var q = this.queues.get(r);

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                EventQueue.this.updateDelayedEvents();
                return !q.isEmpty();
            }

            @Override
            public Event next() {
                EventQueue.this.updateDelayedEvents();
                return q.poll();
            }
        };
    }

    private static class DelayedEvent implements Delayed {
        public final Event e;
        public Instant end;


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
