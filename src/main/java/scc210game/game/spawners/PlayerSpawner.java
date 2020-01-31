package scc210game.game.spawners;

import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.game.components.Health;
import scc210game.game.components.Player;

public class PlayerSpawner implements Spawner {
	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
	    return builder
				.with(new Health(100))
				.with(new Player());
	}
}
