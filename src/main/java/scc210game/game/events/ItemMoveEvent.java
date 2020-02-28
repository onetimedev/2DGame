package scc210game.game.events;

import scc210game.engine.events.Event;
import scc210game.game.components.Inventory;

public class ItemMoveEvent extends Event {
    public final Integer itemID;
    public final Inventory dstInventory;
    public final Integer dstSlot;

    public ItemMoveEvent(Integer itemID, Inventory dstInventory, Integer dstSlot) {
        this.itemID = itemID;
        this.dstInventory = dstInventory;
        this.dstSlot = dstSlot;
    }
}
