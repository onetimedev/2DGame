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
import scc210game.game.events.InventoryUpdateEvent;
import scc210game.game.states.events.LeaveInventoryEvent;

public class TwoInventoryViewState extends InventoryViewStateMethods {
    private final Entity sourceInventoryEnt0;
    private final Inventory sourceInventory0;
    private final Entity sourceInventoryEnt1;
    private final Inventory sourceInventory1;
    private Inventory inventory0;
    private Inventory inventory1;

    public TwoInventoryViewState(World sourceWorld, Entity sourceInventoryEnt0, Inventory sourceInventory0, Entity sourceInventoryEnt1, Inventory sourceInventory1) {
        super(sourceWorld);
        this.sourceInventoryEnt0 = sourceInventoryEnt0;
        this.sourceInventory0 = sourceInventory0;
        this.sourceInventoryEnt1 = sourceInventoryEnt1;
        this.sourceInventory1 = sourceInventory1;
    }

    @Override
    public void onStart(World world) {
        this.inventory0 = this.cloneContentInto(world, this.sourceInventory0);
        this.inventory1 = this.cloneContentInto(world, this.sourceInventory1);

        this.spawnInventory(world, this.inventory0, 0.0f);
        this.spawnInventory(world, this.inventory1, 0.3f);
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof KeyDepressedEvent) {
            KeyDepressedEvent evt1 = (KeyDepressedEvent) evt;
            if (evt1.key == Keyboard.Key.ESCAPE) {
                world.ecs.acceptEvent(new LeaveInventoryEvent());
                world.ecs.eventQueue.broadcast(new InventoryUpdateEvent(
                        new Tuple2<>(this.sourceInventoryEnt0, this.inventory0),
                        new Tuple2<>(this.sourceInventoryEnt1, this.inventory1)));
                return TransNop.getInstance();
            }
        }

        if (evt instanceof LeaveInventoryEvent) {
            return TransPop.getInstance();
        }

        return super.handleEvent(evt, world);
    }

}
