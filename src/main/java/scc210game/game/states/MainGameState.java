package scc210game.game.states;

import scc210game.engine.ecs.World;
import scc210game.game.spawners.MapSpawner;
import scc210game.game.spawners.PlayerSpawner;

public class MainGameState extends BaseInGameState {
	@Override
	public void onStart(World world) {
		world.entityBuilder().with(new MapSpawner()).build();
		world.entityBuilder().with(new PlayerSpawner()).build();
	}
}
