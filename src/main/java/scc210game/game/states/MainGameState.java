package scc210game.game.states;

import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPush;
import scc210game.engine.state.trans.Transition;
import scc210game.game.map.Map;
import scc210game.game.map.Tile;
import scc210game.game.spawners.*;
import scc210game.game.spawners.ui.EnterInventoryButtonSpawner;
import scc210game.game.states.events.EnterInventoryEvent;
import scc210game.game.states.events.EnterTwoInventoryEvent;

public class MainGameState extends BaseInGameState {
	@Override
	public void onStart(World world) {
		world.entityBuilder().with(new MapSpawner()).build();
		world.entityBuilder().with(new PlayerSpawner()).build();
		world.entityBuilder().with(
				new EnterInventoryButtonSpawner(0, 0, 0.05f, 0.05f))
				.build();


		var mapEnt = world.applyQuery(Query.builder().require(Map.class).build()).findFirst().get();
		var map = world.fetchComponent(mapEnt, Map.class);

		for(Tile t : map.getChestTiles()) {
			world.entityBuilder().with(new ChestSpawner(t)).build();
		}

		for(Tile tile : map.getEnemyTiles()) {
			world.entityBuilder().with(new EnemySpawner(tile)).build();
		}

		for(Tile tile : map.getNPCTiles()) {
			world.entityBuilder().with(new NPCSpawner(tile)).build();
		}

		int count = 0;
		for(Vector2i[] v : map.getBossCoords()) {
			world.entityBuilder().with(new BossSpawner(v, count)).build();
			count++;
		}

		//TODO: Move this to GenerateMap?
		Vector2i[] finalBossCoords = new Vector2i[9];
		finalBossCoords[0] = new Vector2i(59,59);
		finalBossCoords[1] = new Vector2i(60,59);
		finalBossCoords[2] = new Vector2i(61,59);
		finalBossCoords[3] = new Vector2i(59,60);
		finalBossCoords[4] = new Vector2i(60,60);
		finalBossCoords[5] = new Vector2i(61,60);
		finalBossCoords[6] = new Vector2i(59,61);
		finalBossCoords[7] = new Vector2i(60,61);
		finalBossCoords[8] = new Vector2i(61,61);

		Tile[] finalBossTiles = new Tile[9];
		for(int i=0; i < finalBossTiles.length; i++)  // Changing tile texture beneath FinalBoss
			map.getTile(finalBossCoords[i].x, finalBossCoords[i].y).setTexture("light_basalt.png");
		world.entityBuilder().with(new FinalBossSpawner()).build();
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

		return super.handleEvent(evt, world);
	}
}
