package scc210game.game.systems;

import org.jsfml.system.Vector2i;
import scc210game.engine.combat.Scoring;
import scc210game.engine.ecs.*;
import scc210game.engine.ecs.System;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.events.LeaveCombatEvent;
import scc210game.engine.movement.Position;
import scc210game.game.components.Dialogue;
import scc210game.game.components.Inventory;
import scc210game.game.components.SelectedWeaponInventory;
import scc210game.game.components.TextureStorage;
import scc210game.game.events.DialogueCreateEvent;
import scc210game.game.map.*;
import scc210game.game.spawners.ChestSpawner;
import scc210game.game.spawners.FilledInventorySpawner;
import scc210game.game.spawners.PlayerSpawner;
import scc210game.game.states.events.EnterTwoInventoryEvent;
import scc210game.game.utils.DialogueHelper;
import scc210game.game.utils.MapHelper;
import javax.annotation.Nonnull;
import javax.swing.*;
import java.time.Duration;
import java.util.Iterator;
import java.util.stream.Stream;

public class CombatLeaveHandler implements System {
	private final EventQueueReader eventReader;

	/**
	 * Constructor creates event reader and listens to LeaveCombatEvent
	 * @param ecs
	 */
	public CombatLeaveHandler(ECS ecs) {
		this.eventReader = ecs.eventQueue.makeReader();
		ecs.eventQueue.listen(this.eventReader, LeaveCombatEvent.class);
	}


	/**
	 * Run method listening for events
	 * @param world
	 * @param timeDelta
	 */
	@Override
	public void run(@Nonnull World world, @Nonnull Duration timeDelta) {

		for (Iterator<Event> it = world.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
			Event e = it.next();
			this.handleEvent(world, e);
		}
	}


	/**
	 * Recieve data from combat system exit event
	 * @param world the world state
	 * @param e the generic event
	 */
	private void handleEvent(World world, Event e) {
		LeaveCombatEvent evt = (LeaveCombatEvent) e;

		var player = world.applyQuery(Query.builder().require(Player.class).build()).findFirst().orElseThrow();

		var scoring = world.fetchComponent(player, Scoring.class);

		scoring.playerExperience = evt.score.playerExperience;  //Update players experience after combat

		DialogueMessage dl = new DialogueMessage();
		if(evt.playerWins) {  // If the player has won
			java.lang.System.out.println("Player Won");
			String msg = enemyDefeated(evt.enemy, world);
			world.eventQueue.broadcast(new DialogueCreateEvent(msg,
					(en, w) -> winReward(world, evt.enemy, player),
					(en, w) -> DialogueHelper.refuse(world, player)));
		}
		else {  // If the player has lost and needs to respawn
			java.lang.System.out.println("Player Lost");
			world.eventQueue.broadcast(new DialogueCreateEvent(dl.getDefeatDialogue(),
					(en, w) -> resetPlayerInventory(player, world),
					(en, w) -> resetPlayerInventory(player, world)));

		}


	}


	/**
	 * Method if the enemy was defeated, works out enemy type for dialog and updates
	 * tile beneath enemy and enemy rendering.
	 * @param enemy the enemy entity as passed from combat
	 * @param world the world for the main game state
	 */
	private String enemyDefeated(Entity enemy, World world) {
		var mapEnt0 = world.applyQuery(Query.builder().require(Map.class).build()).findFirst();
		if (!mapEnt0.isPresent())
			return "";
		var map = mapEnt0.get();
		var mapComp = world.fetchComponent(map, Map.class);

		var enemyPos = world.fetchComponent(enemy, Position.class);

		Query q = Query.builder()  // To get all entities that are enemies
				.require(Enemy.class)
				.build();

		// Stream of all enemy entities changed to array
		Stream<Entity> enemyEntitiesForArray = world.applyQuery(q);
		Entity[] enemyEntArr = enemyEntitiesForArray.toArray(Entity[]::new);

		String msg = "";
		DialogueMessage dm = new DialogueMessage();

		// Loop through all enemy entities, change tile properties. Assign dialog.
		for (Entity enemyEntity : enemyEntArr) {
			var enPos = world.fetchComponent(enemyEntity, Position.class);
			if (enPos.xPos == enemyPos.xPos && enPos.yPos == enemyPos.yPos) {
				var boss = world.fetchComponent(enemyEntity, Boss.class);
				var finalBoss = world.fetchComponent(enemyEntity, FinalBoss.class);

				if (boss == null) {  // ignore warning, it's wrong
					if (finalBoss == null) {
						mapComp.getTile((int) enemyPos.xPos, (int) enemyPos.yPos).setHasEnemy(false);    // Update the tile at the position of the enemy
						mapComp.getTile((int) enemyPos.xPos, (int) enemyPos.yPos).setHasCollision(false);    // Update the tile at the position of the enemy
						msg = dm.getVictoryDialogue();
					}
					else {
						msg = dm.getFinalBossDefeatDialogue();
						// Change all tiles beneath boss sprite to no longer have collision
						Vector2i[] fBossTiles = {new Vector2i(59,59), new Vector2i(60,59), new Vector2i(61,59),
						new Vector2i(59,60), new Vector2i(60,60), new Vector2i(61,60),
						new Vector2i(59, 61), new Vector2i(60, 61), new Vector2i(61, 61)};
						MapHelper.changeTiles(mapComp, fBossTiles, "light_basalt.png",false, false, false);

					}
				}
				else {  // If the enemy is a boss
					var enTexture = world.fetchComponent(enemyEntity, TextureStorage.class);
					String textureName = enTexture.getPath();

					// Defeat dialogue assigned based on biome
					if (textureName.contains("grass")) {
						msg = dm.getGrassBossDefeatDialogue();
						Vector2i[] fBossTiles = {new Vector2i(8,65), new Vector2i(9,65), new Vector2i(8,66), new Vector2i(9,66)};
						MapHelper.changeTiles(mapComp, fBossTiles, "grass.png", false, false);
					}
					else if (textureName.contains("water")) {
						msg = dm.getWaterBossDefeatDialogue();
						Vector2i[] fBossTiles = {new Vector2i(25,23), new Vector2i(26,23), new Vector2i(25,24), new Vector2i(26,24)};
						MapHelper.changeTiles(mapComp, fBossTiles, "sand.png", false, false);
					}
					else if (textureName.contains("fire")) {
						msg = dm.getFireBossDefeatDialogue();
						Vector2i[] fBossTiles = {new Vector2i(107,5), new Vector2i(108,5), new Vector2i(107,6), new Vector2i(108,6)};
						MapHelper.changeTiles(mapComp, fBossTiles, "light_basalt.png", false, false);
					}
					else {
						msg = dm.getIceBossDefeatDialogue();
						Vector2i[] fBossTiles = {new Vector2i(101,100), new Vector2i(102,100), new Vector2i(101,101), new Vector2i(102,101)};
						MapHelper.changeTiles(mapComp, fBossTiles, "ice.png",false, false);
					}
				}

				break;
			}
		}


		// Opening the stream to get all Enemy entities again to directly set the enemys defeated value
		Stream<Entity> enemyEntities = world.applyQuery(q);
		enemyEntities.forEach(en -> {
			var enPos = world.fetchComponent(en, Position.class);
			if (enPos.xPos == enemyPos.xPos && enPos.yPos == enemyPos.yPos) {
				var enComp = world.fetchComponent(en, Enemy.class);
				enComp.defeated = true;

			}
		});


		unlockFinalBoss(world, mapComp);

		return msg;  // Return dialogue message for defeated enemy
	}


	/**
	 * Method to change the barrier tiles to path tiles to access the final boss area
	 * @param world the world for thos state
	 * @param map the map component
	 */
	private void unlockFinalBoss(World world, Map map) {
		Query q = Query.builder()  // To get all entities that are enemies
				.require(Boss.class)
				.build();

		Stream<Entity> bossEntities = world.applyQuery(q);
		Entity[] bossEntArr = bossEntities.toArray(Entity[]::new);
		int defeatedCount = 0;

		for (Entity boss: bossEntArr) {
			var bossEnemyComp = world.fetchComponent(boss, Enemy.class);
			if(bossEnemyComp.defeated)
				defeatedCount++;
		}

		if(defeatedCount == 4) {
			Vector2i[] tiles = {new Vector2i(54,63), new Vector2i(55,64), new Vector2i(56,65),
			new Vector2i(64,65), new Vector2i(65,64), new Vector2i(66,63)};
			MapHelper.changeTiles(map, tiles, "path.png", false, false);

		}


	}


	/**
	 * Method to reset a players inventory after a loss
	 * @param player the player entity
	 * @param world the world for the state
	 */
	private void resetPlayerInventory(Entity player, World world) {
		var playerInv = world.fetchComponent(player, Inventory.class);
		playerInv.clear();
		DialogueHelper.refuse(world, player);

	}


	/**
	 * Method to give the player an item reward for beating an enemy.
	 * @param world the world for the current state
	 * @param enemy the enemy entity
	 * @param player the player entity
	 */
	public void winReward(World world, Entity enemy, Entity player) {
		DialogueHelper.refuse(world, player);
		int rng = (int) (Math.random() * 50);  // Chance of enemy dropping item

		var playerInv = world.fetchComponent(player, Inventory.class);
		var boss = world.fetchComponent(enemy, Boss.class);
		var finalBoss = world.fetchComponent(enemy, FinalBoss.class);


		var selectedWeapon = world.applyQuery(Query.builder().require(SelectedWeaponInventory.class).build()).findFirst().orElseThrow();
		var sw = world.fetchComponent(selectedWeapon, Inventory.class);

		var enemyInv = world.fetchComponent(enemy, Inventory.class);

		if(enemyInv == null)
			java.lang.System.out.println("Enemy Inv Null");
		else {
			if (finalBoss == null) {
				if (boss == null) {
					if (rng > 30) // If it is an enemy then chance of item
						world.ecs.acceptEvent(new EnterTwoInventoryEvent(playerInv, sw, enemyInv, player, selectedWeapon, enemy));
				} else {   // if it is a boss
					world.ecs.acceptEvent(new EnterTwoInventoryEvent(playerInv, sw, enemyInv, player, selectedWeapon, enemy));
				}
			} else {  // If it is the finalboss
				world.ecs.acceptEvent(new EnterTwoInventoryEvent(playerInv, sw, enemyInv, player, selectedWeapon, enemy));

			}
		}



	}



}

