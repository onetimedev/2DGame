package scc210game.game.states.events;

import scc210game.engine.ecs.Entity;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.utils.Tuple2;
import scc210game.game.components.Inventory;

import java.util.List;

public class LeaveInventoryEvent extends StateEvent {
    public final List<Tuple2<Entity, Inventory>> inventories;

    @SafeVarargs
    public LeaveInventoryEvent(Tuple2<Entity, Inventory>... inventories) {
        this.inventories = List.of(inventories);
    }
}
