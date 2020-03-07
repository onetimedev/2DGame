package scc210game.game.spawners;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import scc210game.engine.animation.Animate;
import scc210game.game.combat.CombatUtils;
import scc210game.game.combat.Scoring;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.game.movement.Position;
import scc210game.game.movement.Velocity;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.utils.ResourceLoader;
import scc210game.game.components.*;
import scc210game.game.map.Player;

import java.time.Duration;
import java.util.Set;


/**
 * Class to create a player entity, render the player and assign its components
 */
public class PlayerSpawner implements Spawner {
	private final Texture t;

	/**
	 * Constructor to load the players texture
	 */
	public PlayerSpawner() {
		try {
			this.t = new Texture();
			this.t.loadFromStream(ResourceLoader.resolve("textures/player/player_anim.png"));
		} catch (final Exception e) {
			throw new RuntimeException();
		}
	}


	/**
	 * Method to create the player entity and assign all of its components
	 * @param builder the {@link World.EntityBuilder} to inject into
	 * @param world the World the entity is being built in
	 * @return the player entity
	 */
	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
		world.entityBuilder()
				.with(new Inventory(1))
				.with(new SelectedWeaponInventory())
				.build();
		return builder
				.with(new Player())
				.with(new Inventory(5))
				.with(new Position(15, 106))
				.with(new OldPosition(15, 106))
				.with(new Velocity(0, 0))
				.with(new PlayerLocked(false))
				.with(new Steps(5, 0))
				.with(new Scoring(0, new CombatUtils().STARTING_HEALTH, new CombatUtils().STARTING_HEALTH))
				.with(new TextureStorage("textures/player/player_anim.png"))
				.with(new Animate(Duration.ofMillis((400 * this.t.getSize().x) / 64 - 1), ((e, w) -> {
				}), true))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
						PlayerSpawner::accept));
	}


	/**
	 * Method called by renderable to display the player in the main game window
	 * @param entity the player entity
	 * @param window the main game window
	 * @param world the world for this state
	 */
	private static void accept(Entity entity, RenderWindow window, World world) {
		var playerEnt = world.applyQuery(Query.builder().require(Player.class).build()).findFirst().orElseThrow();
		var position = world.fetchComponent(playerEnt, Position.class);
		var steps = world.fetchComponent(playerEnt, Steps.class);
		var playerTexture = world.fetchComponent(playerEnt, TextureStorage.class);
		var oldPosition = world.fetchComponent(playerEnt, OldPosition.class);
		var animation = world.fetchComponent(playerEnt, Animate.class);

		var sprite = new Sprite(playerTexture.getTexture());

		sprite.setPosition(position.xPos * 64, position.yPos * 64);

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
