package scc210game.ui.systems;

import scc210game.ecs.System;
import scc210game.ecs.World;
import scc210game.events.Event;
import scc210game.events.EventQueue;
import scc210game.events.EventQueueReader;
import scc210game.state.State;
import scc210game.state.event.KeyPressedEvent;
import scc210game.state.event.StateEvent;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

public class HandleInteraction implements System {
    private final EventQueueReader eventReader;

    public HandleInteraction() {
        this.eventReader = EventQueue.makeReader();
        EventQueue.listen(this.eventReader, StateEvent.class);
    }

    @Override
    public void run(@Nonnull World world, @Nonnull Class<? extends State> currentState, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = EventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();

            if (e instanceof KeyPressedEvent) {
                KeyPressedEvent k = (KeyPressedEvent) e;

                // TODO: fill in these events
            }
        }
    }
}
