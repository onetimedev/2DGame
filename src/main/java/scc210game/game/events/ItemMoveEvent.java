package scc210game.game.events;

import scc210game.engine.ecs.Entity;
import scc210game.engine.events.Event;
import scc210game.game.components.Inventory;

public class ItemMoveEvent extends Event {
    public final Entity item;
    public final Inventory dstInventory;
    public final Integer dstSlot;

    public ItemMoveEvent(Entity item, Inventory dstInventory, Integer dstSlot) {
        this.item = item;
        this.dstInventory = dstInventory;
        this.dstSlot = dstSlot;
    }
}
