package scc210game.game.states;

import org.jsfml.window.Keyboard;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.state.event.KeyDepressedEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransNop;
import scc210game.engine.state.trans.TransPop;
import scc210game.engine.state.trans.Transition;
import scc210game.engine.utils.Tuple2;
import scc210game.game.components.Inventory;
import scc210game.game.spawners.ui.BackgroundSpawner;
import scc210game.game.events.InventoryUpdateEvent;
import scc210game.game.states.events.LeaveInventoryEvent;

public class InventoryViewState extends InventoryViewStateMethods {
    private final Entity sourceInventoryEnt;
    private final Inventory sourceInventory;
    private Inventory inventory;

    public InventoryViewState(World sourceWorld, Entity sourceInventoryEnt, Inventory sourceInventory) {
        super(sourceWorld);
        this.sourceInventoryEnt = sourceInventoryEnt;
        this.sourceInventory = sourceInventory;
    }

    @Override
    public void onStart(World world) {
        this.inventory = this.cloneContentInto(world, this.sourceInventory);

        this.spawnInventory(world, this.inventory, 0.1f);

        //TODO: Create background here, custom texture
        world.entityBuilder().with(new BackgroundSpawner("inventory.png")).build();
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof KeyDepressedEvent) {
            KeyDepressedEvent evt1 = (KeyDepressedEvent) evt;
            if (evt1.key == Keyboard.Key.ESCAPE) {
                world.ecs.acceptEvent(new LeaveInventoryEvent());
                world.ecs.eventQueue.broadcast(new InventoryUpdateEvent(
                        new Tuple2<>(this.sourceInventoryEnt, this.inventory)));
                return TransNop.getInstance();
            }
        }

        if (evt instanceof LeaveInventoryEvent) {
            return TransPop.getInstance();
        }

        return super.handleEvent(evt, world);
    }
}
