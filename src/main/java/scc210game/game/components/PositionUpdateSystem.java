package scc210game.game.components;

import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.movement.Velocity;
import scc210game.engine.render.MainViewResource;
import scc210game.game.events.DialogueCreateEvent;
import scc210game.game.map.*;
import scc210game.game.utils.MapHelper;
import scc210game.engine.audio.Audio;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;

public class PositionUpdateSystem implements System {
	Audio au = new Audio();


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

		int tileType = checkBiome(map.getTile((int)position.xPos, (int)position.yPos).getTextureName());

		if(deltaX > 0) {  // right
			if (checkCollisionX(velocity, map, deltaX, right, top, bottom)) return;
			pTexture.texture = MapHelper.loadTexture("player_right.png");
			pTexture.speedMs = 100;
			biomSound(tileType, au);
		}
		if(deltaX < 0) {  // left
			if (checkCollisionX(velocity, map, deltaX, left, top, bottom)) return;
			pTexture.texture = MapHelper.loadTexture("player_left.png");
			pTexture.speedMs = 100;
			biomSound(tileType, au);
		}
		// Y Delta collision checks
		if(deltaY < 0) {  // top
			if (checkCollisionY(velocity, map, deltaY, left, right, top)) return;
			pTexture.texture = MapHelper.loadTexture("player_top.png");
			pTexture.speedMs = 100;
			biomSound(tileType, au);
		}
		if(deltaY > 0) {  // bottom
			if (checkCollisionY(velocity, map, deltaY, left, right, bottom)) return;
			pTexture.texture = MapHelper.loadTexture("player_bottom.png");
			pTexture.speedMs = 100;
			biomSound(tileType, au);
		}
		if(deltaX == 0 && deltaY == 0) {
			au.stopSound();
		}



		// Changing to floored ints to check specific tiles around position
		//int xPosInt = (int) Math.floor(position.xPos + deltaX);
		//int yPosInt = (int) Math.floor(position.yPos + deltaY);
		checkSurrounding(world, map, deltaX, deltaY, position.xPos, position.yPos);

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
	 * @param world
	 * @param map
	 * @param dX
	 * @param dY
	 * @param posX
	 * @param posY
	 */
	public void checkSurrounding(World world, Map map, float dX, float dY, float posX, float posY) {
		var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
		if (!playerEntO.isPresent())
			return;
		var playerEnt = playerEntO.get();
		var steps = world.fetchComponent(playerEnt, Steps.class);


		if(steps.count > steps.oldCount+4) {
			ArrayList<Tile> tiles = getSurrounding(1, map, posX, posY, dX, dY);
			tiles.addAll(getSurrounding(-1, map, posX, posY, dX, dY));

			for (Tile t : tiles) {
				if(t == null)
					continue;
				if (t.getHasEnemy()) {
					if(t.getTextureName().contains("final")) {
						java.lang.System.out.println("FinalBoss nearby");
						world.eventQueue.broadcast(new DialogueCreateEvent(test(world, 4, checkBiome(t.getTextureName())),
								(e, w) -> accept(world),
								(e, w) -> refuse(world)));
					}
					else if(!t.getTextureName().contains("enemy")) {
						java.lang.System.out.println("Boss nearby");
						java.lang.System.out.println(checkBiome(t.getTextureName()));
						world.eventQueue.broadcast(new DialogueCreateEvent(test(world, 3, checkBiome(t.getTextureName())),
								(e, w) -> accept(world),
								(e, w) -> refuse(world)));
					}
					else {
						java.lang.System.out.println("Enemy nearby");
						java.lang.System.out.println(checkBiome(t.getTextureName()));
						world.eventQueue.broadcast(new DialogueCreateEvent(test(world, 0, checkBiome(t.getTextureName())),
								(e, w) -> accept(world),
								(e, w) -> refuse(world)));
					}
					steps.oldCount = steps.count;
					break;
				}
				else if (t.canHaveChest()) {
					java.lang.System.out.println("Chest nearby");
					java.lang.System.out.println(checkBiome(t.getTextureName()));
					world.eventQueue.broadcast(new DialogueCreateEvent(test(world, 2, checkBiome(t.getTextureName())),
							(e, w) -> accept(world),
							(e, w) -> refuse(world)));
					steps.oldCount = steps.count;
					break;
				}
				else if (t.canHaveStory()) {
					java.lang.System.out.println("NPC nearby");
					java.lang.System.out.println(checkBiome(t.getTextureName()));
					world.eventQueue.broadcast(new DialogueCreateEvent(test(world, 1, checkBiome(t.getTextureName())),
							(e, w) -> accept(world),
							(e, w) -> refuse(world)));
					steps.oldCount = steps.count;
					break;
				}
			}
		}

	}


	/**
	 * Method to get all tiles around the player
	 * @param num
	 * @param map
	 * @param posX
	 * @param posY
	 * @param dX
	 * @param dY
	 * @return
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


	private int checkBiome(String t) {
		if(t.contains("grass"))
			return 0;
		else if(t.contains("sand"))
			return 1;
		else if(t.contains("snow") || t.contains("ice"))
			return 3;
		else if(t.contains("basalt"))
			return 2;
		else {
			return 5;
		}
	}


	public void biomSound(int type, Audio au) {
		switch(type) {
			case 0: { //grass
				au.playSound("./src/main/resources/sounds/walking_medium.wav", false);
				break;
			}
			case 1: { //sand
				au.playSound("./src/main/resources/sounds/walking_sand.wav", false);
				break;
			}
			case 2: { //basalt
				//au.playSound("./src/main/resources/sounds/", false);
				break;
			}
			case 3: { //snow
				au.playSound("./src/main/resources/sounds/walking_snow.wav", false);
				break;
			}
			case 5: { //path
				//au.playSound("./src/main/resources/sounds/", false);
				break;
			}
		}
	}

	public String test(World world, int type, int biome) {
		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.zoom(0.6f);
		java.lang.System.out.println("Zoomed in");

		return new DialogueMessage(type, biome).getMessage();
	}

	public void accept(World world) {
		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.zoom(1f/0.6f);
		java.lang.System.out.println("Accepted");
	}

	public void refuse(World world) {
		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.zoom(1f/0.6f);
		java.lang.System.out.println("Refused");
	}

}
