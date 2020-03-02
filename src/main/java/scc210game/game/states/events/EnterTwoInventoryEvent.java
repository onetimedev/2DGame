package scc210game.game.states.events;

import scc210game.engine.ecs.Entity;
import scc210game.engine.state.event.StateEvent;
import scc210game.game.components.Inventory;

public class EnterTwoInventoryEvent extends StateEvent {
    public final Inventory inv0;
    public final Entity inv0Ent;

    public final Inventory inv1;
    public final Entity inv1Ent;

    public EnterTwoInventoryEvent(Inventory inv0, Inventory inv1, Entity inv0Ent, Entity inv1Ent) {
        this.inv0 = inv0;
        this.inv1 = inv1;
        this.inv0Ent = inv0Ent;
        this.inv1Ent = inv1Ent;
    }
}
