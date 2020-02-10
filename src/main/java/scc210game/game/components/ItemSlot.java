package scc210game.game.components;

import scc210game.engine.ecs.Component;

public class ItemSlot extends Component {
    public final Inventory inventory;
    public final int slotID;

    public ItemSlot(Inventory inventory, int slotID) {
        this.inventory = inventory;
        this.slotID = slotID;
    }

    @Override
    public String serialize() {
        return ((Integer) this.slotID).toString();
    }
}

