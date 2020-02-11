package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.utils.MapHelper;
import scc210game.game.map.Enemy;
import scc210game.game.map.FinalBoss;

import java.util.Set;

public class FinalBossSpawner implements Spawner {

	private Texture finalBossTexture;


	public FinalBossSpawner() {
		finalBossTexture = MapHelper.loadTexture("boss_final.png");
	}

	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(new Enemy())
				.with(new FinalBoss())
				.with(new Position(59, 59))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
						(Entity entity, RenderWindow window, World world) -> {

							Sprite en = new Sprite(finalBossTexture);
							en.setPosition(59*64, 59*64);
							window.draw(en);

						}));
	}
	
}
