package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.map.Chest;
import scc210game.game.map.Player;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class ChestSpawner implements Spawner {

	private Vector2i chestCoords;
	private Texture t = new Texture();

	public ChestSpawner(Vector2i coords) {
		chestCoords = coords;
	}

	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(new Chest())
				.with(new Position(chestCoords.x, chestCoords.y))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
						(Entity entity, RenderWindow window, World world) -> {

								Sprite pl = new Sprite(t);
								window.draw(pl);

						}));

	}
}
