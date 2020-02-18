package scc210game.game.components;

import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.movement.Velocity;
import scc210game.engine.render.MainViewResource;
import scc210game.game.map.Map;
import scc210game.game.map.Player;
import scc210game.game.map.Tile;
import scc210game.game.states.events.TriggerChestEvent;
import scc210game.game.states.events.TriggerCombatEvent;
import scc210game.game.states.events.TriggerStoryEvent;

import javax.annotation.Nonnull;
import java.time.Duration;

public class PositionUpdateSystem implements System {


	@Override
	public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
		var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
		if (!playerEntO.isPresent())
			return;
		var playerEnt = playerEntO.get();
		var position = world.fetchComponent(playerEnt, Position.class);
		var velocity = world.fetchComponent(playerEnt, Velocity.class);

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

		// Bounding box position of player
		float left = position.xPos;
		float right = position.xPos + 1;
		float top = position.yPos;
		float bottom = position.yPos + 1;

		// X Delta collision checks
		if(deltaX > 0) {  // right
			if (checkCollisionX(velocity, map, deltaX, right, top, bottom)) return;
		}
		if(deltaX < 0) {  // left
			if (checkCollisionX(velocity, map, deltaX, left, top, bottom)) return;
		}

		// Y Delta collision checks
		if(deltaY < 0) {  // top
			if (checkCollisionY(velocity, map, deltaY, left, right, top)) return;
		}
		if(deltaY > 0) {  // bottom
			if (checkCollisionY(velocity, map, deltaY, left, right, bottom)) return;
		}

		// Changing to floored ints to check specific tiles around position
		int xPosInt = (int) Math.floor(position.xPos + deltaX);
		int yPosInt = (int) Math.floor(position.yPos + deltaY);
		checkSurrounding(world, map, xPosInt, yPosInt);

		// Updating position of player with delta value
		position.xPos += deltaX;
		position.yPos += deltaY;

		// Getting and updating the view by its center
		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.setCenter(position.xPos*64, position.yPos*64);

	}

	/**
	 * Method to check the collision of tiles horizontally around the player
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
	 * @param world to trigger events for the whole game
	 * @param map entity to get the tiles
	 * @param x coordinate of the player
	 * @param y coordinate of the player
	 */
	public void checkSurrounding(World world, Map map, int x, int y) {
		var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
		if (!playerEntO.isPresent())
			return;
		var playerEnt = playerEntO.get();
		var steps = world.fetchComponent(playerEnt, Steps.class);

		if(steps.count > steps.oldCount+4) {
			Tile[] tiles = new Tile[4];
			if(map.legalTile(x + 1, y))
				tiles[0] = map.getTile(x + 1, y);
			if(map.legalTile(x - 1, y))
				tiles[1] = map.getTile(x - 1, y);
			if(map.legalTile(x, y+1))
				tiles[2] = map.getTile(x, y + 1);
			if(map.legalTile(x, y-1))
			tiles[3] = map.getTile(x, y - 1);

			for (Tile t : tiles) {
				if(t == null)
					continue;
				if (t.getHasEnemy()) {
					java.lang.System.out.println("Enemy nearby");
					world.ecs.acceptEvent(new TriggerCombatEvent());
					steps.oldCount = steps.count;
					break;
				}
				else if (t.canHaveChest()) {
					java.lang.System.out.println("Chest nearby");
					world.ecs.acceptEvent(new TriggerChestEvent());
					steps.oldCount = steps.count;
					break;
				}
				else if (t.canHaveStory()) {
					java.lang.System.out.println("NPC nearby");
					world.ecs.acceptEvent(new TriggerStoryEvent());
					steps.oldCount = steps.count;
					break;
				}
			}
		}

	}




}
