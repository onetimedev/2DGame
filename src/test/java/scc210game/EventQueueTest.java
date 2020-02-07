package scc210game;

import org.junit.Test;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class EventQueueTest {
    @Test
    public void testEventQueue() {
        class Event0 extends Event {
            @Nonnull
            final
            String msg = "event0";
        }

        class Event1 extends Event {
            @Nonnull
            final
            String msg = "event1";
        }

        var queue = new EventQueue();

        var r0 = queue.makeReader();
        var r1 = queue.makeReader();

        queue.listen(r0, Event0.class);
        queue.listen(r0, Event1.class);

        queue.listen(r1, Event0.class);

        queue.broadcast(new Event0());
        queue.broadcast(new Event1());

        ArrayList<Event> ent0Events = StreamSupport.stream(
                ((Iterable<Event>) () -> queue.getEventsFor(r0)).spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Event> ent1Events = StreamSupport.stream(
                ((Iterable<Event>) () -> queue.getEventsFor(r1)).spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));

        assertEquals(ent0Events.size(), 2);
        assertEquals(ent1Events.size(), 1);

        queue.unListen(r0, Event0.class);
        queue.broadcast(new Event0());

        ArrayList<Event> ent0Events1 = StreamSupport.stream(
                ((Iterable<Event>) () -> queue.getEventsFor(r0)).spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));

        assertEquals(ent0Events1.size(), 0);
    }
}
