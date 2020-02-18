package scc210game.game.spawners;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Clock;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.map.Player;

import java.nio.file.Paths;
import java.util.Set;


public class PlayerSpawner implements Spawner {

	private Texture t = new Texture();
	private Sprite pl;
	private int frame = 0;
	private Clock animClock = new Clock();

	public PlayerSpawner() {
		try {
			t.loadFromFile(Paths.get("./src/main/resources/textures/player_anim.png"));
			pl = new Sprite(t);
			pl.setTextureRect(new IntRect(0, 0, 64, 64));
		}
		catch(Exception e) {
			throw new RuntimeException();
		}

	}


	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
		return builder
				.with(new Player())
				.with(new Position(15, 106))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
				(Entity e, RenderWindow rw, World w) -> {
					var playerEnt = w.applyQuery(Query.builder().require(Player.class).build()).findFirst().orElseThrow();
					var position = w.fetchComponent(playerEnt, Position.class);

					pl.setPosition(position.xPos*64, position.yPos*64);
					var view = w.fetchGlobalResource(MainViewResource.class);
					view.mainView.setCenter(position.xPos*64, position.yPos*64);

					if(animClock.getElapsedTime().asMilliseconds() >= 500) {
						animClock.restart();
						frame++;
						if(frame > 5)
							frame = 0;
						int frameRow = frame / 8;
						int frameCol = frame % 8;
						pl.setTextureRect(new IntRect(frameCol * 64, frameRow * 64, 64, 64));
					}

					rw.draw(pl);
				}));

	}
}
