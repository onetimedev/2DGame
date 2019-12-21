package scc210game;

import org.junit.Test;
import scc210game.events.Event;
import scc210game.events.EventQueue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class EventQueueTest {
    @Test
    public void testEventQueue() {
        class Event0 implements Event {
            @Nonnull
            final
            String msg = "event0";
        }

        class Event1 implements Event {
            @Nonnull
            final
            String msg = "event1";
        }

        var r0 = EventQueue.makeReader();
        var r1 = EventQueue.makeReader();

        EventQueue.listen(r0, Event0.class);
        EventQueue.listen(r0, Event1.class);

        EventQueue.listen(r1, Event0.class);

        EventQueue.broadcast(new Event0());
        EventQueue.broadcast(new Event1());

        ArrayList<Event> ent0Events = StreamSupport.stream(
                ((Iterable<Event>) () -> EventQueue.getEventsFor(r0)).spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Event> ent1Events = StreamSupport.stream(
                ((Iterable<Event>) () -> EventQueue.getEventsFor(r1)).spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));

        assertEquals(ent0Events.size(), 2);
        assertEquals(ent1Events.size(), 1);

        EventQueue.unListen(r0, Event0.class);
        EventQueue.broadcast(new Event0());

        ArrayList<Event> ent0Events1 = StreamSupport.stream(
                ((Iterable<Event>) () -> EventQueue.getEventsFor(r0)).spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));

        assertEquals(ent0Events1.size(), 0);
    }
}
