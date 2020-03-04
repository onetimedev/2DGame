package scc210game.game.spawners;

import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;

public class FilledInventorySpawner implements Spawner {
    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        var inv = new Inventory(1);

        for (var i = 0; i < 1; i++) {
            var itemEnt = world.entityBuilder()
                    .with(new WeaponSpawner(i + 10))
                    .build();
            var item = world.fetchComponent(itemEnt, Item.class);
            inv.addItem(item.itemID);
        }

        return builder.with(inv);
    }
}
