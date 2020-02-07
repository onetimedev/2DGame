package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.MainViewResource;
import scc210game.game.map.Player;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;


public class PlayerSpawner implements Spawner {
	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(new Player())
				.with(new Position(15, 106))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
				(Entity entity, RenderWindow window, World world) -> {
					Texture t = new Texture();
					try {
						t.loadFromFile(Paths.get("./src/main/resources/textures/basalt.png"));
						Sprite pl = new Sprite(t);


						var playerEnt = world.applyQuery(Query.builder().require(Player.class).build()).findFirst().get();
						var position = world.fetchComponent(playerEnt, Position.class);

						pl.setPosition(position.xPos*64, position.yPos*64);

						var view = world.fetchGlobalResource(MainViewResource.class);
						view.mainView.setCenter(position.xPos*64, position.yPos*64);

						window.draw(pl);

					}
					catch (IOException e) {
						throw new RuntimeException();
					}
				}));

	}
}
