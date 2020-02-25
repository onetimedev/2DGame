package scc210game.game.components;

import scc210game.engine.ecs.*;
import scc210game.engine.ecs.System;
import scc210game.engine.movement.Position;
import scc210game.engine.movement.Velocity;
import scc210game.engine.render.MainViewResource;
import scc210game.game.events.DialogueCreateEvent;
import scc210game.game.map.*;
import scc210game.engine.utils.ResourceLoader;
import scc210game.game.map.Map;
import scc210game.game.map.Player;
import scc210game.game.map.PlayerTexture;
import scc210game.game.map.Tile;
import scc210game.game.states.events.EnterTwoInventoryEvent;
import scc210game.game.utils.MapHelper;
import scc210game.engine.audio.Audio;
import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;

public class PositionUpdateSystem implements System {
	Audio au = new Audio();

	/**
	 * Run method that will continue while the system is active.
	 * Calculates the players velocity and delta X/Y and checks
	 * collision and any surrounding entities.
	 * @param world the world entity
	 * @param timeDelta
	 */
	@Override
	public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
		var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
		if (!playerEntO.isPresent())
			return;
		var playerEnt = playerEntO.get();
		var position = world.fetchComponent(playerEnt, Position.class);
		var velocity = world.fetchComponent(playerEnt, Velocity.class);
		var pTexture = world.fetchComponent(playerEnt, PlayerTexture.class);

		var mapEntO = world.applyQuery(Query.builder().require(Map.class).build()).findFirst();
		if (!mapEntO.isPresent())
			return;
		var mapEnt = mapEntO.get();
		var map = world.fetchComponent(mapEnt, Map.class);

		// Delta of velocity movement around time
		var deltaX = velocity.dx * ((float) timeDelta.toMillis()) / 1000;
		var deltaY = velocity.dy * ((float) timeDelta.toMillis()) / 1000;

		// Decrementing velocity over time
		velocity.dx *= 0.8;
		velocity.dy *= 0.8;

		// Resetting velocity
		if(velocity.dx > -0.1 && velocity.dx < 0.1)
			velocity.dx = 0;
		if(velocity.dy > -0.1 && velocity.dy < 0.1)
			velocity.dy = 0;


		if(velocity.dx == 0 && velocity.dy == 0) {
			pTexture.texture = MapHelper.loadTexture("player_anim.png");
			pTexture.speedMs = 400;
		}


		// Bounding box position of player
		float left = position.xPos;
		float right = position.xPos + 1;
		float top = position.yPos;
		float bottom = position.yPos + 1;

		// X Delta collision checks
		if(deltaX > 0) {  // right
			if (checkCollisionX(velocity, map, deltaX, right, top, bottom))
				deltaX = 0;
			else {
				pTexture.texture = MapHelper.loadTexture("player_right.png");
				pTexture.speedMs = 100;
				au.playSound(ResourceLoader.resolve("sounds/walking_medium.wav"), false);
			}
		}
		if(deltaX < 0) {  // left
			if (checkCollisionX(velocity, map, deltaX, left, top, bottom))
				deltaX = 0;
			else {
				pTexture.texture = MapHelper.loadTexture("player_left.png");
				pTexture.speedMs = 100;
				au.playSound(ResourceLoader.resolve("sounds/walking_medium.wav"), false);
			}
		}

		// Y Delta collision checks
		if(deltaY < 0) {  // top
			if (checkCollisionY(velocity, map, deltaY, left, right, top))
				deltaY = 0;
			else {
				pTexture.texture = MapHelper.loadTexture("player_top.png");
				pTexture.speedMs = 100;
				au.playSound(ResourceLoader.resolve("sounds/walking_medium.wav"), false);
			}
		}
		if(deltaY > 0) {  // bottom
			if (checkCollisionY(velocity, map, deltaY, left, right, bottom))
				deltaY = 0;
			else {
				pTexture.texture = MapHelper.loadTexture("player_bottom.png");
				pTexture.speedMs = 100;
				au.playSound(ResourceLoader.resolve("sounds/walking_medium.wav"), false);

			}
		}

		if(deltaX == 0 && deltaY == 0) {
			au.stopSound();
		}

		checkSurrounding(world, map, deltaX, deltaY, position.xPos, position.yPos);

		// Updating position of player with delta value
		position.xPos += deltaX;
		position.yPos += deltaY;

		// Getting and updating the view by its center
		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.setCenter(position.xPos * 64, position.yPos * 64);

	}


	/**
	 * Method to check the collision of tiles horizontally around the player
	 *
	 * @param velocity of the player
	 * @param map entity that holds the tiles
	 * @param deltaY delta value of Y axis
	 * @param left bounds
	 * @param right bounds
	 * @param bottom bounds
	 * @return true if the tiles in the direction of movement have collision, false otherwise
	 */
	private boolean checkCollisionY(Velocity velocity, Map map, float deltaY, float left, float right, float bottom) {
		if (map.legalTile((int) Math.floor(left + 0.2), (int) Math.floor(bottom + deltaY)))
			if (map.getTile((int) Math.floor(left + 0.2), (int) Math.floor(bottom + deltaY)).hasCollision()) {
				velocity.dy = 0;
				return true;
			}
		if (map.legalTile((int) Math.floor(right - 0.2), (int) Math.floor(bottom + deltaY)))
			if (map.getTile((int) Math.floor(right - 0.2), (int) Math.floor(bottom + deltaY)).hasCollision()) {
				velocity.dy = 0;
				return true;
			}
		return false;
	}


	/**
	 * Method to check the collision of tiles horizontally around the player
	 *
	 * @param velocity of the player
	 * @param map entity that holds the tiles
	 * @param deltaX delta value of X axis
	 * @param right bounds
	 * @param top bounds
	 * @param bottom bounds
	 * @return true if the tiles in the direction of movement have collision, false otherwise
	 */
	private boolean checkCollisionX(Velocity velocity, Map map, float deltaX, float right, float top, float bottom) {
		if (map.legalTile((int) Math.floor(right + deltaX), (int) Math.floor(top + 0.2)))
			if (map.getTile((int) Math.floor(right + deltaX), (int) Math.floor(top + 0.2)).hasCollision()) {
				velocity.dx = 0;
				return true;
			}
		if (map.legalTile((int) Math.floor(right + deltaX), (int) Math.floor(bottom - 0.2)))
			if (map.getTile((int) Math.floor(right + deltaX), (int) Math.floor(bottom - 0.2)).hasCollision()) {
				velocity.dx = 0;
				return true;
			}
		return false;
	}


	/**
	 * Method that checks the surrounding tiles for presence of a chest, enemy, or NPC
	 * @param world the world for the current state
	 * @param map the map entity
	 * @param dX delta X
	 * @param dY delta Y
	 * @param posX players X position
	 * @param posY players Y position
	 */
	public void checkSurrounding(World world, Map map, float dX, float dY, float posX, float posY) {
		var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
		if (!playerEntO.isPresent())
			return;
		var playerEnt = playerEntO.get();
		var steps = world.fetchComponent(playerEnt, Steps.class);

		// Cooldown check, player needs to have moved 4+ tiles before another dialogue / entity trigger
		if(steps.count > steps.oldCount+4) {
			ArrayList<Tile> tiles = getSurrounding(1, map, posX, posY, dX, dY);
			tiles.addAll(getSurrounding(-1, map, posX, posY, dX, dY));

			for (Tile t : tiles) {  // Checks each surrounding tile
				if(t == null)
					continue;
				if (t.getHasEnemy()) {  // Enemy checks
					if(t.getTextureName().contains("final")) {
						java.lang.System.out.println("FinalBoss nearby");
						world.eventQueue.broadcast(new DialogueCreateEvent(inDialogue(world, playerEnt,4, checkBiome(t.getTextureName())),
								(e, w) -> accept(world, 0, playerEnt, null),
								(e, w) -> refuse(world, playerEnt)));
					}
					else if(!t.getTextureName().contains("enemy")) {
						java.lang.System.out.println("Boss nearby");
						java.lang.System.out.println(checkBiome(t.getTextureName()));
						world.eventQueue.broadcast(new DialogueCreateEvent(inDialogue(world, playerEnt,3, checkBiome(t.getTextureName())),
								(e, w) -> accept(world, 0, playerEnt, null),
								(e, w) -> refuse(world, playerEnt)));
					}
					else {
						java.lang.System.out.println("Enemy nearby");
						java.lang.System.out.println(checkBiome(t.getTextureName()));
						world.eventQueue.broadcast(new DialogueCreateEvent(inDialogue(world, playerEnt,0, checkBiome(t.getTextureName())),
								(e, w) -> accept(world, 0, playerEnt, null),
								(e, w) -> refuse(world, playerEnt)));
					}
					steps.oldCount = steps.count;
					break;
				}
				else if (t.canHaveChest()) {  // Chest check
					java.lang.System.out.println("Chest nearby");
					var chestEnt = getEntityAtPos(world, t, Chest.class);
					java.lang.System.out.println(checkBiome(t.getTextureName()));
					world.eventQueue.broadcast(new DialogueCreateEvent(inDialogue(world, playerEnt,2, checkBiome(t.getTextureName())),
							(e, w) -> accept(world, 1, playerEnt, chestEnt),
							(e, w) -> refuse(world, playerEnt)));
					steps.oldCount = steps.count;
					break;
				}
				else if (t.canHaveStory()) {  // NPC check
					java.lang.System.out.println("NPC nearby");
					java.lang.System.out.println(checkBiome(t.getTextureName()));
					world.eventQueue.broadcast(new DialogueCreateEvent(inDialogue(world, playerEnt,1, checkBiome(t.getTextureName())),
							(e, w) -> accept(world, 2, playerEnt, null),
							(e, w) -> refuse(world, playerEnt)));
					steps.oldCount = steps.count;
					break;
				}
			}
		}
	}


	/**
	 * Method to check the single entity against a position
	 * @param world the world for the state
	 * @param t tile being checked
	 * @param klass component class being searched for
	 * @return
	 */
	private Entity getEntityAtPos(World world, Tile t, Class<? extends Component> klass) {
		return world.applyQuery(Query.builder().require(klass).build()).filter(e -> {
			var pos = world.fetchComponent(e, Position.class);
			return pos.xPos == t.getXPos() && pos.yPos == t.getYPos();
		}).findFirst().orElseThrow();
	}


	/**
	 * Method to get all tiles around the player
	 * @param num positive or negative to be checked
	 * @param map the map entity
	 * @param posX players X position
	 * @param posY players Y position
	 * @param dX delta X
	 * @param dY delta Y
	 * @return all tiles surrounding player
	 */
	private ArrayList<Tile> getSurrounding(int num, Map map, float posX, float posY, float dX, float dY) {
		int xFloor = (int) Math.floor(posX + dX);
		int yFloor = (int) Math.floor(posY + dY);
		int xCeil = (int) Math.ceil(posX + dX);
		int yCeil = (int) Math.ceil(posY + dY);

		ArrayList<Tile> tiles = new ArrayList<>();
		tiles.add(map.getTile(xCeil, yCeil + num));
		tiles.add(map.getTile(xFloor, yCeil + num));
		tiles.add(map.getTile(xCeil, yFloor + num));
		tiles.add(map.getTile(xFloor, yFloor + num));
		tiles.add(map.getTile(xCeil + num, yCeil));
		tiles.add(map.getTile(xCeil + num, yFloor));
		tiles.add(map.getTile(xFloor + num, yCeil));
		tiles.add(map.getTile(xFloor + num, yFloor));

		return tiles;
	}


	/**
	 * Method to check the biome given a texture name as string
	 * @param t name of texture / string to be checked
	 * @return the biome number (Grass = 0, Water = 1, Fire = 2, Ice = 3))
	 */
	private int checkBiome(String t) {
		if(t.contains("grass"))
			return 0;
		else if(t.contains("sand"))
			return 1;
		else if(t.contains("snow") || t.contains("ice"))
			return 3;
		else
			return 2;
	}


	/**
	 * Method for when the player is viewing a dialogue, displays a random message
	 * based on the entity type and biome.
	 * @param world the world for the state
	 * @param player the player entity
	 * @param type the type of entity (Enemy = 0, NPC = 1, Chest = 2, Boss = 3, FinalBoss = 4)
	 * @param biome the type of biome (Grass = 0, Water = 1, Fire = 2, Ice = 3)
	 * @return the message to be displayed in the dialogue
	 */
	public String inDialogue(World world, Entity player, int type, int biome) {
		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.zoom(0.6f);
		java.lang.System.out.println("Zoomed in");
		var positionLocked = world.fetchComponent(player, PlayerLocked.class);
		positionLocked.locked = true;

		return new DialogueMessage(type, biome).getMessage();
	}


	/**
	 * Method triggered upon player acceptance of an entities dialogue option.
	 * @param world the world for this state
	 * @param eventType the type of entity event that should be triggered
	 * @param player the player entity
	 */
	public void accept(World world, int eventType, Entity player, Entity target) {
		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.zoom(1f/0.6f);

		var positionLocked = world.fetchComponent(player, PlayerLocked.class);
		positionLocked.locked = false;

		switch(eventType) {  // Checking which event should be sent to the queue
			case 0: {
				java.lang.System.out.println("Combat State Initiated");
				//world.ecs.acceptEvent(new CombatStateEvent());  TODO: Need to know params that should be passed to combat
			}
			case 1: {
				java.lang.System.out.println("Chest State Initiated");
				var playerInv = world.fetchComponent(player, Inventory.class);
				var targetInv = world.fetchComponent(target, Inventory.class);
				world.ecs.acceptEvent(new EnterTwoInventoryEvent(playerInv, targetInv, player, target));
			}

		}

	}


	/**
	 * Method triggered upon player refusal of an entities dialogue option.
	 * @param world the world for this state
	 * @param player the player entity
	 */
	public void refuse(World world, Entity player) {
		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.zoom(1f/0.6f);
		var positionLocked = world.fetchComponent(player, PlayerLocked.class);
		positionLocked.locked = false;
	}

}
