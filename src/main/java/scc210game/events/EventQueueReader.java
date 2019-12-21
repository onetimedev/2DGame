package scc210game.events;

import java.util.Iterator;

/**
 * A reader for an event queue
 */
public class EventQueueReader {
    private final long id;

    EventQueueReader(long id) {
        this.id = id;
    }

    /**
     * Get events for this reader
     *
     * @return an iterator of events that this reader has received
     */
    public Iterator<Event> getEvents() {
        return EventQueue.getEventsFor(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        EventQueueReader that = (EventQueueReader) o;

        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (this.id ^ (this.id >>> 32));
    }
}
