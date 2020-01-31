package scc210game.engine.ui.systems;

import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.events.EntityClickEvent;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueue;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.ui.components.UIClickable;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

/**
 * A class that manages invoking the UIClickable callback on clicked entities
 */
public class HandleClicked implements System {
    private final EventQueueReader eventReader;

    public HandleClicked() {
        this.eventReader = EventQueue.makeReader();
        EventQueue.listen(this.eventReader, EntityClickEvent.class);
    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = EventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();

            if (e instanceof EntityClickEvent) {
                EntityClickEvent e1 = (EntityClickEvent) e;
                var clickable = world.fetchComponent(e1.entity, UIClickable.class);
                clickable.acceptor.accept(e1.entity, world);
            }
        }
    }
}
