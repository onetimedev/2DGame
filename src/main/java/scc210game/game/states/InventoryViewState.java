package scc210game.game.states;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.World;
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;

public class InventoryViewState extends BaseInGameState {
    private World sourceWorld;
    private Inventory sourceInventory;

    public InventoryViewState(World sourceWorld, Inventory sourceInventory) {
        this.sourceWorld = sourceWorld;
        this.sourceInventory = sourceInventory;
    }

    @Override
    public void onStart(World world) {
        this.cloneContentInto(world);

    }

    /**
     * Copy the inventory data from one world to another
     * @param destWorld the world to copy the inventory into
     */
    private void cloneContentInto(World destWorld) {
        var inventory = new Inventory(this.sourceInventory.slotCount);
        var inventoryEnt = destWorld.entityBuilder()
                .with(inventory)
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
            inventory.addItemToSlot(itemEnt, item, v.l);
        });
    }
}
