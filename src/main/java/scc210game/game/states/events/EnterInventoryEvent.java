package scc210game.game.states.events;

import scc210game.engine.ecs.Entity;
import scc210game.engine.state.event.StateEvent;
import scc210game.game.components.Inventory;
import scc210game.game.components.Inventory;

public class EnterInventoryEvent extends StateEvent {
    public final Inventory inv;
    public final Entity invEnt;
    public final Entity selectedItemEnt;
    public final Inventory selectedItemInventory;

    public EnterInventoryEvent(Inventory inv, Inventory selectedItemInventory, Entity invEnt, Entity selectedItemEnt) {
        this.inv = inv;
        this.invEnt = invEnt;
        this.selectedItemEnt = selectedItemEnt;
        this.selectedItemInventory = selectedItemInventory;
    }
}
