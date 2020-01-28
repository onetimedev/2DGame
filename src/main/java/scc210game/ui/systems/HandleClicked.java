package scc210game.ui.systems;

import scc210game.ecs.System;
import scc210game.ecs.World;
import scc210game.events.*;
import scc210game.ui.components.UIClickable;
import scc210game.ui.components.UIDroppable;
import scc210game.ui.components.UITransform;

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
