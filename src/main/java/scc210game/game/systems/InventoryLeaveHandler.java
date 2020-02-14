package scc210game.game.systems;

import scc210game.engine.ecs.ECS;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.game.components.Inventory;
import scc210game.game.states.events.LeaveInventoryEvent;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

/**
 * Acts to move inventory modifications from one world to another
 */
public class InventoryLeaveHandler implements System {
    private final EventQueueReader eventReader;

    public InventoryLeaveHandler(ECS ecs) {
        this.eventReader = ecs.eventQueue.makeReader();
        ecs.eventQueue.listen(this.eventReader, LeaveInventoryEvent.class);
    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = world.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();

            if (e instanceof LeaveInventoryEvent) {
                LeaveInventoryEvent e1 = (LeaveInventoryEvent) e;
                for (var inv: e1.inventories) {
                    var srcInv = world.fetchComponent(inv.l, Inventory.class);
                    srcInv.clear();

                    inv.r.items().forEach(t -> srcInv.addItemToSlot(t.l, t.r));
                }
            }
        }
    }

}
