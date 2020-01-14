package scc210game.render;

import scc210game.ecs.World;
import scc210game.map.MapSpawner;
import scc210game.state.State;

public class BasicState implements State {
	@Override
	public void onStart(World world) {
		world.entityBuilder().with(new MapSpawner()).build();
	}
}
