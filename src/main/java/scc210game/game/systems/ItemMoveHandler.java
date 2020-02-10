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

    private Entity findSourceInventory(Entity itemEntity, World world) {
        return world.applyQuery(inventoriesQuery)
                .filter(e -> world.fetchComponent(e, Inventory.class).getItemSlot(itemEntity) != null)
                .findFirst()
                .orElseThrow();
    }

    private Entity findSlot(Inventory inventory, int slotID, World world) {
        return world.applyQuery(uiItemSlotQuery)
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
                var item = world.fetchComponent(e1.item, Item.class);

                var srcInventoryEnt = this.findSourceInventory(e1.item, world);
                var srcInventory = world.fetchComponent(srcInventoryEnt, Inventory.class);

                // move item from src
                srcInventory.removeItem(e1.item, item);
                // to dst
                e1.dstInventory.addItemToSlot(e1.item, item, e1.dstSlot);

                // move the item to reflect the new position
                var uiSlotEnt = this.findSlot(e1.dstInventory, e1.dstSlot, world);

                var itemTrans = world.fetchComponent(e1.item, UITransform.class);
                var slotTrans = world.fetchComponent(uiSlotEnt, UITransform.class);

                var centerPosition = UiUtils.centerTransforms(itemTrans.size(), slotTrans.pos(), slotTrans.size());

                itemTrans.updateOrigin(centerPosition.x, centerPosition.y);
            }
        }
    }
}
