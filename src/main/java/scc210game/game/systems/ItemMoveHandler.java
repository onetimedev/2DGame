package scc210game.game.systems;

import scc210game.engine.ecs.System;
import scc210game.engine.ecs.*;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;
import scc210game.game.components.ItemSlot;
import scc210game.game.events.ItemMoveEvent;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

public class ItemMoveHandler implements System {
    private final EventQueueReader eventReader;
    private final Query itemQuery = Query.builder()
            .require(Item.class)
            .build();
    private final Query inventoriesQuery = Query.builder()
            .require(Inventory.class)
            .build();
    private final Query uiItemSlotQuery = Query.builder()
            .require(ItemSlot.class)
            .build();

    public ItemMoveHandler(ECS ecs) {
        this.eventReader = ecs.eventQueue.makeReader();
        ecs.eventQueue.listen(this.eventReader, ItemMoveEvent.class);
    }

    private Entity findSourceInventory(int itemID, World world) {
        return world.applyQuery(this.inventoriesQuery)
                .filter(e -> world.fetchComponent(e, Inventory.class).getSlotID(itemID).isPresent())
                .findFirst()
                .orElseThrow();
    }

    private Entity findItem(int itemID, World world) {
        return world.applyQuery(this.itemQuery)
                .filter(e -> world.hasComponent(e, Item.class))
                .filter(e -> world.fetchComponent(e, Item.class).itemID == itemID)
                .findFirst()
                .orElseThrow();
    }

    private Entity findSlot(Inventory inventory, int slotID, World world) {
        return world.applyQuery(this.uiItemSlotQuery)
                .filter(e -> {
                    var slot = world.fetchComponent(e, ItemSlot.class);
                    return slot.inventory == inventory && slot.slotID == slotID;
                })
                .findFirst()
                .orElseThrow();
    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = world.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();

            if (e instanceof ItemMoveEvent) {
                ItemMoveEvent e1 = (ItemMoveEvent) e;
                var itemEnt = this.findItem(e1.itemID, world);

                var srcInventoryEnt = this.findSourceInventory(e1.itemID, world);
                var srcInventory = world.fetchComponent(srcInventoryEnt, Inventory.class);

                // move item from src
                srcInventory.removeItem(e1.itemID);
                // to dst
                e1.dstInventory.addItemToSlot(e1.itemID, e1.dstSlot);

                // move the item to reflect the new position
                var uiSlotEnt = this.findSlot(e1.dstInventory, e1.dstSlot, world);

                var itemTrans = world.fetchComponent(itemEnt, UITransform.class);
                var slotTrans = world.fetchComponent(uiSlotEnt, UITransform.class);

                var centerPosition = UiUtils.centerTransforms(itemTrans.size(), slotTrans.pos(), slotTrans.size());

                itemTrans.updateOrigin(centerPosition.x, centerPosition.y);
            }
        }
    }
}
