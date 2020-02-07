package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.map.Enemy;
import scc210game.game.map.FinalBoss;

import java.util.Set;

public class FinalBossSpawner implements Spawner {

	public FinalBossSpawner() {

	}



	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(new Enemy())
				.with(new FinalBoss())
				.with(new Position(0, 0))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
						(Entity entity, RenderWindow window, World world) -> {

							//Sprite en = new Sprite(enemyTexture);
							//en.setPosition(xSpawn*64, ySpawn*64);
							//window.draw(en);

						}));
	}
	
}
