package scc210game.spawners;

import scc210game.ecs.Spawner;
import scc210game.ecs.World;
import scc210game.map.Player;

public class PlayerSpawner implements Spawner {


	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(new Player());

	}
}
