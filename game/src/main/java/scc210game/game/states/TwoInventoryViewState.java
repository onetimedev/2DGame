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
import scc210game.game.components.Inventory;
import scc210game.game.spawners.ui.BackgroundSpawner;
import scc210game.game.events.InventoryUpdateEvent;
import scc210game.game.states.events.LeaveInventoryEvent;

import java.awt.*;

public class TwoInventoryViewState extends InventoryViewStateMethods {
    private final Entity sourceInventoryEnt0;
    private final Inventory sourceInventory0;
    private final Entity sourceSelectedItemEnt;
    private final Inventory sourceSelectedItemInventory;
    private final Entity sourceInventoryEnt1;
    private final Inventory sourceInventory1;

    private Inventory inventory0;
    private Inventory selectedItemInventory;
    private Inventory inventory1;

    public TwoInventoryViewState(World sourceWorld, Entity sourceInventoryEnt0, Inventory sourceInventory0, Entity sourceSelectedItemEnt, Inventory sourceSelectedItemInventory, Entity sourceInventoryEnt1, Inventory sourceInventory1) {
        super(sourceWorld);
        this.sourceInventoryEnt0 = sourceInventoryEnt0;
        this.sourceInventory0 = sourceInventory0;
        this.sourceSelectedItemEnt = sourceSelectedItemEnt;
        this.sourceSelectedItemInventory = sourceSelectedItemInventory;
        this.sourceInventoryEnt1 = sourceInventoryEnt1;
        this.sourceInventory1 = sourceInventory1;
    }

    @Override
    public void onStart(World world) {
        this.inventory0 = this.cloneContentInto(world, this.sourceInventory0);
        this.inventory1 = this.cloneContentInto(world, this.sourceInventory1);
        this.selectedItemInventory = this.cloneContentInto(world, this.sourceSelectedItemInventory);

        this.spawnInventory(world, this.inventory0, 0.1f, 0.0f);
        var selectedSlotOffs = this.inventory0.slotCount * (SLOT_SIZE + SLOT_SPACING);
        this.spawnInventory(world, this.selectedItemInventory, 0.1f, selectedSlotOffs, new Color(90, 55, 120));
        this.spawnInventory(world, this.inventory1, 0.33f, 0.0f);

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
                        new Tuple2<>(this.sourceInventoryEnt0, this.inventory0),
                        new Tuple2<>(this.sourceSelectedItemEnt, this.selectedItemInventory),
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
