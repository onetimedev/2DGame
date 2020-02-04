package scc210game.engine.ui.systems;

import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.events.*;
import scc210game.engine.ui.components.UIHovered;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

/**
 * A class that manages adding the Hovered component to entities that are being hovered
 */
public class HandleHovered implements System {
    private final EventQueueReader eventReader;

    public HandleHovered() {
        this.eventReader = EventQueue.makeReader();
        EventQueue.listen(this.eventReader, EntityHoverStartEvent.class);
        EventQueue.listen(this.eventReader, EntityHoverStopEvent.class);
    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = EventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();

            if (e instanceof EntityHoverStartEvent) {
                EntityHoverStartEvent e1 = (EntityHoverStartEvent) e;
                world.addComponentToEntity(e1.entity, new UIHovered());
            } else if (e instanceof EntityHoverStopEvent) {
                EntityHoverStopEvent e1 = (EntityHoverStopEvent) e;
                world.removeComponentFromEntity(e1.entity, UIHovered.class);
            }
        }
    }
}
