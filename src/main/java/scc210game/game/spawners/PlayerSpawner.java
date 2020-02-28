package scc210game.game.spawners;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import scc210game.engine.animation.Animate;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.movement.Velocity;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.components.Inventory;
import scc210game.game.components.Steps;
import scc210game.game.map.Player;
import scc210game.game.map.PlayerLocked;
import scc210game.engine.utils.ResourceLoader;
import scc210game.game.components.OldPosition;
import scc210game.game.components.TextureStorage;
import java.time.Duration;
import java.util.Set;


public class PlayerSpawner implements Spawner {
	private final Texture t;

	public PlayerSpawner() {
		try {
			this.t = new Texture();
			this.t.loadFromFile(ResourceLoader.resolve("textures/player_anim.png"));
		} catch (final Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
		return builder
				.with(new Player())
				.with(new Inventory(5))
				.with(new Position(15, 106))
				.with(new OldPosition(15, 106))
				.with(new Velocity(0, 0))
				.with(new PlayerLocked(false))
				.with(new Steps(5, 0))
				.with(new TextureStorage("textures/player_anim.png"))
				.with(new Animate(Duration.ofMillis((400 * this.t.getSize().x) / 64 - 1), ((e, w) -> {
				}), true))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
						PlayerSpawner::accept));
	}

	private static void accept(Entity entity, RenderWindow window, World world) {
		var playerEnt = world.applyQuery(Query.builder().require(Player.class).build()).findFirst().orElseThrow();
		var position = world.fetchComponent(playerEnt, Position.class);
		var steps = world.fetchComponent(playerEnt, Steps.class);
		var playerTexture = world.fetchComponent(playerEnt, TextureStorage.class);
		var oldPosition = world.fetchComponent(playerEnt, OldPosition.class);
		var animation = world.fetchComponent(playerEnt, Animate.class);

		var sprite = new Sprite(playerTexture.getTexture());

		sprite.setPosition(position.xPos * 64, position.yPos * 64);

		//System.out.println("POS:" + Math.floor(position.xPos) + "," + Math.floor(position.yPos));
		if (oldPosition.xPos != Math.floor(position.xPos) || oldPosition.yPos != Math.floor(position.yPos))
			steps.count++;

		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.setCenter(position.xPos * 64, position.yPos * 64);

		var numFrames = (playerTexture.getTexture().getSize().x / 64);

		var frame = (int) Math.floor(animation.pctComplete * (float) numFrames);

		int frameRow = frame / 8;
		int frameCol = frame % 8;
		sprite.setTextureRect(new IntRect(frameCol * 64, frameRow * 64, 64, 64));

		oldPosition.xPos = (int) Math.floor(position.xPos);
		oldPosition.yPos = (int) Math.floor(position.yPos);

		window.draw(sprite);
	}
}
