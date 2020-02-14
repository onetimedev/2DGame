package scc210game.game.states;

import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.game.map.Map;
import scc210game.game.map.Tile;
import scc210game.game.spawners.*;

public class MainGameState extends BaseInGameState {
	@Override
	public void onStart(World world) {
		System.out.println("onStart");
		world.entityBuilder().with(new MapSpawner()).build();
		System.out.println("After map");
		world.entityBuilder().with(new PlayerSpawner()).build();


		var mapEnt = world.applyQuery(Query.builder().require(Map.class).build()).findFirst().get();
		var map = world.fetchComponent(mapEnt, Map.class);

		// Spawning of all Chests
		for(Tile t : map.getChestTiles()) {
			world.entityBuilder().with(new ChestSpawner(t)).build();
		}

		// Spawning of all Enemies
		for(Tile tile : map.getEnemyTiles()) {
			world.entityBuilder().with(new EnemySpawner(tile)).build();
		}

		for(Tile tile : map.getNPCTiles()) {
			world.entityBuilder().with(new NPCSpawner(tile)).build();
		}

		int count = 0;
		for(Vector2i[] v : map.getBossCoords()) {
			world.entityBuilder().with(new BossSpawner(v, count, map)).build();
			count++;
		}



		// Hardcoded final boss coordinates
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

		for(int i=0; i < finalBossTiles.length; i++) {  // Changing tile texture beneath FinalBoss
			map.getTile(finalBossCoords[i].x, finalBossCoords[i].y).setTexture("light_basalt.png");
			map.getTile(finalBossCoords[i].x, finalBossCoords[i].y).setHasEnemy(true);
		}
		world.entityBuilder().with(new FinalBossSpawner()).build();




	}
}
