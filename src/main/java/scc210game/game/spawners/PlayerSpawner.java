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

	private final Texture t = new Texture();
	private Vector2i oldCoords = new Vector2i(15, 106);
	private final Sprite pl;
	private int frame = 0;
	private final Clock animClock = new Clock();

	public PlayerSpawner() {
		try {
			this.t.loadFromFile(Paths.get("./src/main/resources/textures/player_anim.png"));
			this.pl = new Sprite(this.t);
			this.pl.setTextureRect(new IntRect(0, 0, 64, 64));
		} catch (final Exception e) {
			throw new RuntimeException();
		}

	}


	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
		return builder
				.with(new Player())
				.with(new Position(15, 106))
				.with(new Velocity(0, 0))
				.with(new Steps(5, 0))
				.with(new PlayerTexture(this.t, 400))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
				(Entity e, RenderWindow rw, World w) -> {
					var playerEnt = w.applyQuery(Query.builder().require(Player.class).build()).findFirst().orElseThrow();
					var position = w.fetchComponent(playerEnt, Position.class);
					var steps = w.fetchComponent(playerEnt, Steps.class);
					var pTexture = w.fetchComponent(playerEnt, PlayerTexture.class);

					this.pl.setTexture(pTexture.texture);
					this.pl.setPosition(position.xPos * 64, position.yPos * 64);

					//System.out.println("POS:" + Math.floor(position.xPos) + "," + Math.floor(position.yPos));
					if (this.oldCoords.x != Math.floor(position.xPos) || this.oldCoords.y != Math.floor(position.yPos))
						steps.count++;

					var view = world.fetchGlobalResource(MainViewResource.class);
					view.mainView.setCenter(position.xPos * 64, position.yPos * 64);

					if (this.animClock.getElapsedTime().asMilliseconds() >= pTexture.speedMs) {
						this.animClock.restart();
						this.frame++;
						if (this.frame > (pTexture.texture.getSize().x / 64) - 1)
							this.frame = 0;
						int frameRow = this.frame / 8;
						int frameCol = this.frame % 8;
						this.pl.setTextureRect(new IntRect(frameCol * 64, frameRow * 64, 64, 64));
					}

					this.oldCoords = new Vector2i((int) Math.floor(position.xPos), (int) Math.floor(position.yPos));
					rw.draw(this.pl);
				}));

	}
}
