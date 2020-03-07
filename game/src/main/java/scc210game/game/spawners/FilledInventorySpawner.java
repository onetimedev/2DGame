package scc210game.game.spawners;

import scc210game.game.combat.Scoring;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;
import scc210game.game.map.Player;

public class FilledInventorySpawner implements Spawner {


    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        var inv = new Inventory(1);

        var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
        playerEntO.orElseThrow();
        var playerEnt = playerEntO.get();
        var scores = world.fetchComponent(playerEnt, Scoring.class);

        for (var i = 0; i < 1; i++) {
            var itemEnt = world.entityBuilder()
                    .with(new WeaponSpawner(scores.playerExperience + 8))
                    .build();
            var item = world.fetchComponent(itemEnt, Item.class);
            inv.addItem(item.itemID);
        }

        return builder.with(inv);
    }
}
