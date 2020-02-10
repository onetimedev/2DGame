package scc210game.game.states;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.World;
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;
import scc210game.game.spawners.ui.InventorySlotSpawner;

public class InventoryViewState extends BaseInGameState {
    private static final int SLOTS_PER_ROW = 7;
    private static final float SLOT_SIZE = 0.05f;
    private static final float SLOT_SPACING = 0.001f;

    private final World sourceWorld;
    private final Inventory sourceInventory;
    private Inventory inventory;

    public InventoryViewState(World sourceWorld, Inventory sourceInventory) {
        this.sourceWorld = sourceWorld;
        this.sourceInventory = sourceInventory;
    }

    @Override
    public void onStart(World world) {
        this.cloneContentInto(world);

        for (int i = 0; i < this.inventory.slotCount; i++) {
            var x = (i % SLOTS_PER_ROW) * (SLOT_SIZE + SLOT_SPACING);
            var y = (i / SLOTS_PER_ROW) * (SLOT_SIZE + SLOT_SPACING);
            world.entityBuilder()
                    .with(new InventorySlotSpawner(x, y, SLOT_SIZE, SLOT_SIZE, this.inventory, i))
                    .build();
        }
    }

    /**
     * Copy the inventory data from one world to another
     *
     * @param destWorld the world to copy the inventory into
     */
    private void cloneContentInto(World destWorld) {
        this.inventory = new Inventory(this.sourceInventory.slotCount);
        var inventoryEnt = destWorld.entityBuilder()
                .with(this.inventory)
                .build();

        // clone all inventory
        this.sourceInventory.items().forEach(v -> {
            var itemEnt = destWorld.entityBuilder()
                    .with(this.sourceWorld.componentsOfEntity(v.r).map(c -> {
                        try {
                            return c.clone();
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    }).toArray(Component[]::new))
                    .build();
            var item = destWorld.fetchComponent(itemEnt, Item.class);
            this.inventory.addItemToSlot(itemEnt, item, v.l);
        });
    }
}
