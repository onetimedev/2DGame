package scc210game.game.spawners;

import scc210game.game.combat.Scoring;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;
import scc210game.game.map.Player;

public class FilledInventorySpawner implements Spawner {
    private final int level;

    public FilledInventorySpawner(int level) {
        this.level = level;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        var inv = new Inventory(5);

        var itemEnt = world.entityBuilder()
                .with(new WeaponSpawner(this.level))
                .build();
        var item = world.fetchComponent(itemEnt, Item.class);
        inv.addItem(item.itemID);

        return builder.with(inv);
    }
}
