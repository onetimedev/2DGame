package scc210game.game.spawners;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.movement.Velocity;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.components.Steps;
import scc210game.game.map.Player;
import scc210game.game.map.PlayerTexture;

import java.nio.file.Paths;
import java.util.Set;


public class PlayerSpawner implements Spawner {

	private Texture t = new Texture();
	private Vector2i oldCoords = new Vector2i(15, 106);
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
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(new Player())
				.with(new Position(15, 106))
				.with(new Velocity(0, 0))
				.with(new Steps(5, 0))
				.with(new PlayerTexture(t, 400))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
				(Entity entity, RenderWindow window, World world) -> {
					var playerEnt = world.applyQuery(Query.builder().require(Player.class).build()).findFirst().orElseThrow();
					var position = world.fetchComponent(playerEnt, Position.class);
					var steps = world.fetchComponent(playerEnt, Steps.class);
					var pTexture = world.fetchComponent(playerEnt, PlayerTexture.class);

					pl.setTexture(pTexture.texture);
					pl.setPosition(position.xPos*64, position.yPos*64);

					//System.out.println("POS:" + Math.floor(position.xPos) + "," + Math.floor(position.yPos));
					if(oldCoords.x != Math.floor(position.xPos) || oldCoords.y != Math.floor(position.yPos))
						steps.count++;

					var view = world.fetchGlobalResource(MainViewResource.class);
					view.mainView.setCenter(position.xPos*64, position.yPos*64);

					if(animClock.getElapsedTime().asMilliseconds() >= pTexture.speedMs) {
						animClock.restart();
						frame++;
						if(frame > (pTexture.texture.getSize().x/64)-1)
							frame = 0;
						int frameRow = frame / 8;
						int frameCol = frame % 8;
						pl.setTextureRect(new IntRect(frameCol * 64, frameRow * 64, 64, 64));
					}

					oldCoords = new Vector2i((int) Math.floor(position.xPos), (int) Math.floor(position.yPos));
					window.draw(pl);
				}));

	}
}
