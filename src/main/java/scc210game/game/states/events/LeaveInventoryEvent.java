package scc210game.game.states.events;

import scc210game.engine.state.event.StateEvent;
import scc210game.game.components.Inventory;

import java.util.List;

public class LeaveInventoryEvent extends StateEvent {
    public final List<Inventory> inventories;

    public LeaveInventoryEvent(Inventory... inventories) {
        this.inventories = List.of(inventories);
    }
}
