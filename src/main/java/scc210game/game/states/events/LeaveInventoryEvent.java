package scc210game.game.states.events;

import scc210game.engine.state.event.StateEvent;
import scc210game.game.components.Inventory;

public class LeaveInventoryEvent extends StateEvent {
    public final Inventory inventory;

    public LeaveInventoryEvent(Inventory inventory) {
        this.inventory = inventory;
    }
}
