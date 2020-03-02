package scc210game.game.states.events;

import scc210game.engine.ecs.Entity;
import scc210game.engine.state.event.StateEvent;
import scc210game.game.components.Inventory;

public class EnterInventoryEvent extends StateEvent {
    public final Inventory inv;
    public final Entity invEnt;

    public EnterInventoryEvent(Inventory inv, Entity invEnt) {
        this.inv = inv;
        this.invEnt = invEnt;
    }
}
