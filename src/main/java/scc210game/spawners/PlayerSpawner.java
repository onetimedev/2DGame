package scc210game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;
import scc210game.ecs.Entity;
import scc210game.ecs.Spawner;
import scc210game.ecs.World;
import scc210game.map.Map;
import scc210game.map.Player;
import scc210game.movement.Position;
import scc210game.render.Renderable;
import scc210game.render.ViewType;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class PlayerSpawner implements Spawner {


	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(new Player())
				.with(new Position(11, 53))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
				(Entity entity, RenderWindow window, World world) -> {

					Texture t = new Texture();
					try {
						t.loadFromFile(Paths.get("./src/main/assets/basalt.png"));
						Sprite pl = new Sprite(t);
						window.draw(pl);
					}
					catch (IOException e) {
						throw new RuntimeException();
					}



				}));

	}
}
