package scc210game.render;

import scc210game.ecs.World;
import scc210game.spawners.MapSpawner;
import scc210game.spawners.PlayerSpawner;
import scc210game.state.State;
import scc210game.ui.spawners.DialogueSpawner;

public class BasicState implements State {
	@Override
	public void onStart(World world) {
		world.entityBuilder().with(new MapSpawner()).build();
		world.entityBuilder().with(new PlayerSpawner()).build();
	}
}
