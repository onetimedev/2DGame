package scc210game.game.systems;

import scc210game.engine.animation.Animate;
import scc210game.engine.combat.Scoring;
import scc210game.engine.ecs.*;
import scc210game.engine.ecs.System;
import scc210game.engine.movement.Position;
import scc210game.engine.movement.Velocity;
import scc210game.engine.render.MainViewResource;
import scc210game.game.components.*;
import scc210game.game.events.DialogueCreateEvent;
import scc210game.game.map.*;
import scc210game.game.map.Map;
import scc210game.game.map.Player;
import scc210game.game.map.Tile;
import scc210game.game.states.events.EnterTwoInventoryEvent;
import scc210game.game.states.events.TriggerCombatEvent;
import scc210game.game.utils.MapHelper;
import scc210game.engine.audio.Audio;
import javax.annotation.Nonnull;
import javax.swing.*;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.NoSuchElementException;

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
		var pTexture = world.fetchComponent(playerEnt, TextureStorage.class);
		var pAnimate = world.fetchComponent(playerEnt, Animate.class);

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
			pTexture.reloadTexture("textures/player/player_anim.png");
			pAnimate.updateDuration(Duration.ofMillis((400 * pTexture.getTexture().getSize().x) / 64));
		}


		// Bounding box position of player
		float left = position.xPos;
		float right = position.xPos + 1;
		float top = position.yPos;
		float bottom = position.yPos + 1;

		int tileType = MapHelper.checkBiome(map.getTile((int)position.xPos, (int)position.yPos).getTextureName());
		this.au.changeBiome(tileType);

		if(deltaX > 0) {  // right
			if (this.checkCollisionX(velocity, map, deltaX, right, top, bottom))
				deltaX = 0;
			else {
				pTexture.reloadTexture("textures/player/player_right.png");
				pAnimate.updateDuration(Duration.ofMillis((100 * pTexture.getTexture().getSize().x) / 64));
				this.biomeSound(tileType, au);			}
		}
		if(deltaX < 0) {  // left
			if (this.checkCollisionX(velocity, map, deltaX, left, top, bottom))
				deltaX = 0;
			else {
				pTexture.reloadTexture("textures/player/player_left.png");
				pAnimate.updateDuration(Duration.ofMillis((100 * pTexture.getTexture().getSize().x) / 64));
				this.biomeSound(tileType, au);			}
		}

		// Y Delta collision checks
		if(deltaY < 0) {  // top
			if (this.checkCollisionY(velocity, map, deltaY, left, right, top))
				deltaY = 0;
			else {
				pTexture.reloadTexture("textures/player/player_top.png");
				pAnimate.updateDuration(Duration.ofMillis((100 * pTexture.getTexture().getSize().x) / 64));
				this.biomeSound(tileType, au);
			}
		}
		if(deltaY > 0) {  // bottom
			if (this.checkCollisionY(velocity, map, deltaY, left, right, bottom))
				deltaY = 0;
			else {
				pTexture.reloadTexture("textures/player/player_bottom.png");
				pAnimate.updateDuration(Duration.ofMillis((100 * pTexture.getTexture().getSize().x) / 64));
				this.biomeSound(tileType, au);
			}
		}

		if(deltaX == 0 && deltaY == 0) {
			this.au.stopSound();
		}

		this.checkSurrounding(world, map, deltaX, deltaY, position.xPos, position.yPos);

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
						world.eventQueue.broadcast(new DialogueCreateEvent(inDialogue(world, playerEnt,4, MapHelper.checkBiome(t.getTextureName())),
								(e, w) -> acceptCombat(world, playerEnt, 2, getEntityAtPos(world, t, Enemy.class)),  //hardcoded biometype
								(e, w) -> refuse(world, playerEnt)));
					}
					else if(!t.getTextureName().contains("enemy")) {
						java.lang.System.out.println("Boss nearby");
						java.lang.System.out.println(MapHelper.checkBiome(t.getTextureName()));
						world.eventQueue.broadcast(new DialogueCreateEvent(inDialogue(world, playerEnt,3, MapHelper.checkBiome(t.getTextureName())),
								(e, w) -> acceptCombat(world, playerEnt, MapHelper.checkBiome(t.getTextureName()), getEntityAtPos(world, t, Enemy.class)),
								(e, w) -> refuse(world, playerEnt)));
					}
					else {
						java.lang.System.out.println("Enemy nearby: " + t.getTextureName());
						java.lang.System.out.println(MapHelper.checkBiome(t.getTextureName()));
						world.eventQueue.broadcast(new DialogueCreateEvent(inDialogue(world, playerEnt,0, MapHelper.checkBiome(t.getTextureName())),
								(e, w) -> acceptCombat(world, playerEnt, MapHelper.checkBiome(t.getTextureName()), getEntityAtPos(world, t, Enemy.class)),
								(e, w) -> refuse(world, playerEnt)));
					}
					steps.oldCount = steps.count;
					break;
				}
				else if (t.canHaveChest()) {  // Chest check
					java.lang.System.out.println("Chest nearby");
					var chestEnt = getEntityAtPos(world, t, Chest.class);
					java.lang.System.out.println(MapHelper.checkBiome(t.getTextureName()));
					world.eventQueue.broadcast(new DialogueCreateEvent(inDialogue(world, playerEnt,2, MapHelper.checkBiome(t.getTextureName())),
							(e, w) -> acceptChest(world, playerEnt, chestEnt),
							(e, w) -> refuse(world, playerEnt)));
					steps.oldCount = steps.count;
					break;
				}
				else if (t.canHaveStory()) {  // NPC check
					java.lang.System.out.println("NPC nearby");
					java.lang.System.out.println(MapHelper.checkBiome(t.getTextureName()));
					world.eventQueue.broadcast(new DialogueCreateEvent(inDialogue(world, playerEnt,1, MapHelper.checkBiome(t.getTextureName())),
							(e, w) -> refuse(world, playerEnt),
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
	 * Method to play the appropriate sound for what the player is walking on
	 * @param type of surface
	 * @param au audio
	 */
	public void biomeSound(int type, Audio au) {
		switch(type) {
			case 0: { //grass
				au.playSound(Paths.get("./src/main/resources/sounds/walking_medium.wav"), false);
				break;
			}
			case 1: { //sand
				au.playSound(Paths.get("./src/main/resources/sounds/walking_sand.wav"), false);
				break;
			}
			case 2: { //basalt
				au.playSound(Paths.get("./src/main/resources/sounds/walking_gravel.wav"), false);
				break;
			}
			case 3: { //snow
				au.playSound(Paths.get("./src/main/resources/sounds/walking_snow.wav"), false);
				break;
			}
			case 5: { //path
				au.playSound(Paths.get("./src/main/resources/sounds/walking_path.wav"), false);
				break;
			}
		}
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

		java.lang.System.out.println("Type: " + type + ", Biome: " + biome);

		return new DialogueMessage(type, biome).getMessage();
	}


	/**
	 * Method triggered upon player acceptance of an entities dialogue option.
	 * @param world the world for this state
	 * @param player the player entity
	 */
	public void acceptChest(World world, Entity player, Entity target) {
		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.zoom(1f/0.6f);

		var positionLocked = world.fetchComponent(player, PlayerLocked.class);
		positionLocked.locked = false;

		java.lang.System.out.println("Chest State Initiated");
		var playerInv = world.fetchComponent(player, Inventory.class);


		var selectedWeapon = world.applyQuery(Query.builder().require(SelectedWeaponInventory.class).build()).findFirst().orElseThrow();
		var sw = world.fetchComponent(selectedWeapon, Inventory.class);

		var targetInv = world.fetchComponent(target, Inventory.class);
		world.ecs.acceptEvent(new EnterTwoInventoryEvent(playerInv, sw, targetInv, player, selectedWeapon, target));
	}


	/**
	 *
	 * @param world the world for this state
	 * @param player the player entity
	 * @param biomeType the type of biome  0=Grass, 1=Sand, 2=Fire, 3=Snow
	 * @param enemy the enemy entity
	 */
	public void acceptCombat(World world, Entity player, int biomeType, Entity enemy) {
		java.lang.System.out.println("Combat State Initiated");
		var enemyDamage = world.fetchComponent(enemy, Enemy.class);

		var enemyTexture = world.fetchComponent(enemy, TextureStorage.class);
		String root = "src/main/resources/textures/";
		String combatPath = "Combat/Enlarged/";
		String textureName = "";
		String background = "";
		switch(biomeType) {
				case 0: {
					background = root + "Combat/combat-bground-grass.png";
					if(enemyTexture.getPath().contains("boss"))
						textureName = root + combatPath + "Earth-Boss-Combat-Animation-LARGE.png";
					else
						textureName = root + combatPath + "Earth-Enemy-Combat-Animation-LARGE.png";
					break;
				}
				case 1: {
					background = root + "Combat/combat-background-water.png";
					if(enemyTexture.getPath().contains("boss"))
						textureName = root + combatPath + "Water-Boss-Combat-Animation-LARGE.png";
					else
						textureName = root + combatPath + "Water-Enemy-Combat-Animation-LARGE.png";
					break;
				}
				case 2: {
					background = root + "Combat/combat-background-lava.png";
					if(enemyTexture.getPath().contains("boss"))
						textureName = root + combatPath + "Fire-Boss-Combat-Animation-LARGE.png";
					else
						textureName = root + combatPath + "Fire-Enemy-Combat-Animation-LARGE.png";
					break;
				}
				case 3: {
					background = root + "Combat/combat-background-ice.png";
					if(enemyTexture.getPath().contains("boss"))
						textureName = root + combatPath + "Ice-Boss-Combat-Animation-LARGE.png";
					else
						textureName = root + combatPath + "Ice-Enemy-Combat-Animation-LARGE.png";
					break;
				}
			}


			// Getting inventory to get currently equipped item to pass into combat
			var invEntO = world.applyQuery(Query.builder().require(SelectedWeaponInventory.class).build()).findFirst();
			if (!invEntO.isPresent())
				return;
			var invEnt = invEntO.get();
			var invComp = world.fetchComponent(invEnt, Inventory.class);

			var item = findItem(invComp.items().findFirst().orElseThrow().l, world);  // Isolate item entity from stream of items

			var itemTextureStorage = world.fetchComponent(item, TextureStorage.class);


			var scores = world.fetchComponent(player, Scoring.class);

			world.ecs.acceptEvent(new TriggerCombatEvent(scores, textureName, itemTextureStorage, background, enemyDamage.damage, enemy));

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


	/**
	 * Method taken from InventoryViewStateMethods, needed here for SelectedWeaponInventory
	 * @param itemID
	 * @param world
	 * @return
	 */
	protected Entity findItem(int itemID, World world) {
		Query itemQuery = Query.builder()
				.require(Item.class)
				.build();

		return world.applyQuery(itemQuery)
				.filter(e -> world.hasComponent(e, Item.class))
				.filter(e -> world.fetchComponent(e, Item.class).itemID == itemID)
				.findFirst()
				.orElseThrow();
	}

}
