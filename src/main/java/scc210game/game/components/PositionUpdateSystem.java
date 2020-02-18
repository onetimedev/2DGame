package scc210game.game.components;

import org.jsfml.system.Vector2f;
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
		//java.lang.System.out.println(velocity.dx + " " + velocity.dy);

		var deltaX = velocity.dx * ((float) timeDelta.toMillis()) / 1000;  // for actual movement
		var deltaY = velocity.dy * ((float) timeDelta.toMillis()) / 1000;



		//java.lang.System.out.println("PRE * velX: " + velocity.dx + " velY: " + velocity.dy + ". xPos: " + xPosition + " yPos: " + yPosition);

		velocity.dx *= 0.8;
		velocity.dy *= 0.8;

		//java.lang.System.out.println("PRE IF velX: " + velocity.dx + " velY: " + velocity.dy + ". xPos: " + xPosition + " yPos: " + yPosition);

		if(velocity.dx > -0.1 && velocity.dx < 0.1)
			velocity.dx = 0;
		if(velocity.dy > -0.1 && velocity.dy < 0.1)
			velocity.dy = 0;

		//java.lang.System.out.println("velX: " + velocity.dx + " velY: " + velocity.dy + ". xPos: " + xPosition + " yPos: " + yPosition);







		java.lang.System.out.println("PLAYER COORDS: " + position.xPos + "," + position.yPos);


		java.lang.System.out.println("Velocity: " + velocity.dx + "," + velocity.dy);


		float left = position.xPos;
		float right = position.xPos + 1;
		float top = position.yPos;
		float bottom = position.yPos + 1;

		if(deltaX > 0) {  // right
			if (map.legalTile((int) Math.floor(right + deltaX), (int) Math.floor(top + 0.2)))
				if (map.getTile((int) Math.floor(right + deltaX), (int) Math.floor(top + 0.2)).hasCollision()) {
					java.lang.System.out.println("PlayerCoords: " + position.xPos+  "," + position.yPos);
					java.lang.System.out.println("TileCoords: " + (int) Math.floor(right + deltaX) + "," + (int) Math.floor(top + 10));
					java.lang.System.out.println("COLLISION");
					velocity.dy = 0;
					velocity.dx = 0;
					return;
				}
			if (map.legalTile((int) Math.floor(right + deltaX), (int) Math.floor(bottom - 0.2)))
				if (map.getTile((int) Math.floor(right + deltaX), (int) Math.floor(bottom - 0.2)).hasCollision()) {
					java.lang.System.out.println("PlayerCoords: " + position.xPos+  "," + position.yPos);
					java.lang.System.out.println("TileCoords: " + (int) Math.floor(right + deltaX) + "," + (int) Math.floor(top + 10));
					java.lang.System.out.println("COLLISION");
					velocity.dy = 0;
					velocity.dx = 0;
					return;
				}
		}
		if(deltaX < 0) {
			if (map.legalTile((int) Math.floor(left + deltaX), (int) Math.floor(top + 0.2)))
				if (map.getTile((int) Math.floor(left + deltaX), (int) Math.floor(top + 0.2)).hasCollision()) {
					java.lang.System.out.println("PlayerCoords: " + position.xPos+  "," + position.yPos);
					java.lang.System.out.println("TileCoords: " + (int) Math.floor(right + deltaX) + "," + (int) Math.floor(top + 10));
					java.lang.System.out.println("COLLISION");
					velocity.dy = 0;
					velocity.dx = 0;
					return;
				}
			if (map.legalTile((int) Math.floor(left + deltaX), (int) Math.floor(bottom - 0.2)))
				if (map.getTile((int) Math.floor(left + deltaX), (int) Math.floor(bottom - 0.2)).hasCollision()) {
					java.lang.System.out.println("PlayerCoords: " + position.xPos+  "," + position.yPos);
					java.lang.System.out.println("TileCoords: " + (int) Math.floor(right + deltaX) + "," + (int) Math.floor(top + 10));
					java.lang.System.out.println("COLLISION");
					velocity.dy = 0;
					velocity.dx = 0;
					return;
				}
			}


		if(deltaY < 0) {  // right
			if (map.legalTile((int) Math.floor(left + 0.2), (int) Math.floor(top + deltaY)))
				if (map.getTile((int) Math.floor(left + 0.2), (int) Math.floor(top + deltaY)).hasCollision()) {
					java.lang.System.out.println("PlayerCoords: " + position.xPos+  "," + position.yPos);
					java.lang.System.out.println("TileCoords: " + (int) Math.floor(right + deltaX) + "," + (int) Math.floor(top + 10));
					java.lang.System.out.println("COLLISION");
					velocity.dy = 0;
					velocity.dx = 0;
					return;
				}
			if (map.legalTile((int) Math.floor(right - 0.2), (int) Math.floor(top + deltaY)))
				if (map.getTile((int) Math.floor(right - 0.2), (int) Math.floor(top + deltaY)).hasCollision()) {
					java.lang.System.out.println("PlayerCoords: " + position.xPos+  "," + position.yPos);
					java.lang.System.out.println("TileCoords: " + (int) Math.floor(right + deltaX) + "," + (int) Math.floor(top + 10));
					java.lang.System.out.println("COLLISION");
					velocity.dy = 0;
					velocity.dx = 0;
					return;
				}
		}
		if(deltaY > 0) {
			if (map.legalTile((int) Math.floor(left + 0.2), (int) Math.floor(bottom + deltaY)))
				if (map.getTile((int) Math.floor(left + 0.2), (int) Math.floor(bottom + deltaY)).hasCollision()) {
					java.lang.System.out.println("PlayerCoords: " + position.xPos+  "," + position.yPos);
					java.lang.System.out.println("TileCoords: " + (int) Math.floor(right + deltaX) + "," + (int) Math.floor(top + 10));
					java.lang.System.out.println("COLLISION");
					velocity.dy = 0;
					velocity.dx = 0;
					return;
				}
			if (map.legalTile((int) Math.floor(right - 0.2), (int) Math.floor(bottom + deltaY)))
				if (map.getTile((int) Math.floor(right - 0.2), (int) Math.floor(bottom + deltaY)).hasCollision()) {
					java.lang.System.out.println("PlayerCoords: " + position.xPos+  "," + position.yPos);
					java.lang.System.out.println("TileCoords: " + (int) Math.floor(right + deltaX) + "," + (int) Math.floor(top + 10));
					java.lang.System.out.println("COLLISION");
					velocity.dy = 0;
					velocity.dx = 0;
					return;
				}
		}


		int xPosInt = (int) Math.floor(position.xPos + deltaX);
		int yPosInt = (int) Math.floor(position.yPos + deltaY);

		checkSurrounding(world, map, xPosInt, yPosInt);


		position.xPos += deltaX;
		position.yPos += deltaY;

		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.setCenter(position.xPos*64, position.yPos*64);

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
