package scc210game.game.events;

import scc210game.engine.ecs.Entity;
import scc210game.engine.events.Event;
import scc210game.engine.utils.Tuple2;
import scc210game.game.components.Inventory;

import java.util.List;

public class InventoryUpdateEvent extends Event {
    public final List<Tuple2<Entity, Inventory>> inventories;

    @SafeVarargs
    public InventoryUpdateEvent(Tuple2<Entity, Inventory>... inventories) {
        this.inventories = List.of(inventories);
    }
}