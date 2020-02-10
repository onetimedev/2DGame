package scc210game.game.states.events;

import scc210game.engine.state.event.StateEvent;
import scc210game.game.components.Inventory;

public class EnterInventoryEvent extends StateEvent {
    public final Inventory inv;

    public EnterInventoryEvent(Inventory inv) {
        this.inv = inv;
    }
}
