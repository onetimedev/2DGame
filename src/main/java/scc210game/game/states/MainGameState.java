package scc210game.game.states;

import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPush;
import scc210game.engine.state.trans.Transition;
import scc210game.game.events.DialogueCreateEvent;
import scc210game.game.map.Map;
import scc210game.game.map.Tile;
import scc210game.game.spawners.*;
import scc210game.game.states.events.CombatStateEvent;
import scc210game.game.states.events.TriggerCombatEvent;

public class MainGameState extends BaseInGameState {
	@Override
	public void onStart(World world) {
		System.out.println("onStart");
		world.entityBuilder().with(new MapSpawner()).build();
		System.out.println("After map");
		world.entityBuilder().with(new PlayerSpawner()).build();


		var mapEnt = world.applyQuery(Query.builder().require(Map.class).build()).findFirst().orElseThrow();
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

		world.entityBuilder().with(new FinalBossSpawner()).build();

		world.eventQueue.broadcast(new DialogueCreateEvent("hello, press q to ignore, enter to accept",
				(e, w) -> System.out.println("Accepted"),
				(e, w) -> System.out.println("Ignored")));
	}

	@Override
	public Transition handleEvent(StateEvent evt, World world) {
		if(evt instanceof TriggerCombatEvent){
			return new TransPush(new CombatState());
		}
		return super.handleEvent(evt, world);
	}
}
