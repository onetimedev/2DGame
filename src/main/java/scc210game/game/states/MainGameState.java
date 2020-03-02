package scc210game.game.states;

import org.jsfml.system.Vector2i;
import scc210game.engine.combat.Scoring;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPush;
import scc210game.engine.state.trans.Transition;
import scc210game.game.components.CombatData;
import scc210game.game.events.DialogueCreateEvent;
import scc210game.game.map.Map;
import scc210game.game.map.Player;
import scc210game.game.map.Tile;
import scc210game.game.spawners.*;
import scc210game.game.states.events.CombatStateEvent;
import scc210game.game.states.events.TriggerCombatEvent;
import scc210game.game.spawners.ui.EnterInventoryButtonSpawner;
import scc210game.game.states.events.EnterInventoryEvent;
import scc210game.game.states.events.EnterTwoInventoryEvent;

public class MainGameState extends BaseInGameState {
	static {
		register(MainGameState.class, (j) -> new MainGameState());
	}

	@Override
	public void onStart(World world) {
        world.entityBuilder().with(new MapSpawner()).build();
        world.entityBuilder().with(new PlayerSpawner()).build();
        world.entityBuilder().with(
                new EnterInventoryButtonSpawner(0, 0, 0.05f, 0.05f))
                .build();


        var mapEnt = world.applyQuery(Query.builder().require(Map.class).build()).findFirst().orElseThrow();
        var map = world.fetchComponent(mapEnt, Map.class);

        // Spawning of all Chests
        for (final Tile t : map.getChestTiles()) {
            world.entityBuilder().with(new ChestSpawner(t)).build();
        }

        // Spawning of all Enemies
        for (final Tile tile : map.getEnemyTiles()) {
            world.entityBuilder().with(new EnemySpawner(tile, 5)).build();
        }

        for (final Tile tile : map.getNPCTiles()) {
            world.entityBuilder().with(new NPCSpawner(tile)).build();
        }

        int count = 0;
        for (final Vector2i[] v : map.getBossCoords()) {
            world.entityBuilder().with(new BossSpawner(v, count, map, 10)).build();
            count++;
        }

        world.entityBuilder().with(new FinalBossSpawner(15)).build();

    }

	@Override
	public Transition handleEvent(StateEvent evt, World world) {
		if (evt instanceof EnterInventoryEvent) {
			EnterInventoryEvent evt1 = (EnterInventoryEvent) evt;
			return new TransPush(new InventoryViewState(world, evt1.invEnt, evt1.inv));
		}

		if (evt instanceof EnterTwoInventoryEvent) {
			EnterTwoInventoryEvent evt1 = (EnterTwoInventoryEvent) evt;
			return new TransPush(new TwoInventoryViewState(world, evt1.inv0Ent, evt1.inv0, evt1.inv1Ent, evt1.inv1));
		}

		if(evt instanceof TriggerCombatEvent){
			var playerEnt = world.applyQuery(Query.builder().require(Player.class).build()).findFirst().orElseThrow();
			var combatData = world.fetchComponent(playerEnt, CombatData.class);

			return new TransPush(new CombatState(combatData));
		}


		return super.handleEvent(evt, world);


	}


}
