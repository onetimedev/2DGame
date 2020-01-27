package scc210game.ui.systems;

import scc210game.ecs.System;
import scc210game.ecs.World;
import scc210game.events.*;
import scc210game.ui.components.UITransform;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

/**
 * A class that manages adding the Dragged component to entities that are being hovered
 */
public class HandleDragged implements System {
    private final EventQueueReader eventReader;

    public HandleDragged() {
        this.eventReader = EventQueue.makeReader();
        EventQueue.listen(this.eventReader, EntityDraggedEvent.class);
        EventQueue.listen(this.eventReader, EntityDroppedEvent.class);
        EventQueue.listen(this.eventReader, EntityFailedDroppedEvent.class);
    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = EventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();

            if (e instanceof EntityDraggedEvent) {
                EntityDraggedEvent e1 = (EntityDraggedEvent) e;
                var transform = world.fetchComponent(e1.entity, UITransform.class);
                transform.xPos = transform.originXPos + e1.translX;
                transform.yPos = transform.originYPos + e1.translY;
            } else if (e instanceof EntityFailedDroppedEvent) {
                EntityFailedDroppedEvent e1 = (EntityFailedDroppedEvent) e;
                var transform = world.fetchComponent(e1.entity, UITransform.class);
                transform.xPos = transform.originXPos;
                transform.yPos = transform.originYPos;
            } else if (e instanceof EntityDroppedEvent) {
                EntityDroppedEvent e1 = (EntityDroppedEvent) e;
                var transform = world.fetchComponent(e1.dropped, UITransform.class);
                transform.xPos = transform.originXPos;
                transform.yPos = transform.originYPos;
            }
        }
    }
}
