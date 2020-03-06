package scc210game.engine.events;

/**
 * A reader for an event queue
 */
public class EventQueueReader {
    private final long id;

    EventQueueReader(long id) {
        this.id = id;
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
