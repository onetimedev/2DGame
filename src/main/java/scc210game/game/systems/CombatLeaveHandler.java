package scc210game.game.systems;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import scc210game.engine.combat.Scoring;
import scc210game.engine.ecs.*;
import scc210game.engine.ecs.System;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.events.LeaveCombatEvent;
import scc210game.engine.movement.Position;
import scc210game.game.components.TextureStorage;
import scc210game.game.events.DialogueCreateEvent;
import scc210game.game.map.*;
import scc210game.game.utils.DialogueHelper;
import scc210game.game.utils.MapHelper;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;
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

		for (Iterator<Event> it = world.ecs.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
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
		var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
		if (!playerEntO.isPresent())
			return;
		var player = playerEntO.get();
		var scoring = world.fetchComponent(player, Scoring.class);

		LeaveCombatEvent evt = (LeaveCombatEvent) e;
		scoring.playerExperience = evt.score.playerExperience;  //Update players experience after combat

		world.deactivateCombat();

		DialogueMessage dl = new DialogueMessage();
		if(evt.playerWins) {  // If the player has won
			String msg = enemyDefeated(evt.enemy, world);
			world.eventQueue.broadcast(new DialogueCreateEvent(msg,
					(en, w) -> DialogueHelper.refuse(world, player),
					(en, w) -> DialogueHelper.refuse(world, player)));
		}
		else {  // If the player has lost and needs to respawn
			world.eventQueue.broadcast(new DialogueCreateEvent(dl.getDefeatDialogue(),
					(en, w) -> DialogueHelper.refuse(world, player),  // TODO: Respawning / loss of items etc needs to be done here
					(en, w) -> DialogueHelper.refuse(world, player))); // TODO: Respawning / loss of items etc needs to be done here OR

			//TODO: Respawning here after dialogue closed
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
						MapHelper.changeTiles(mapComp, fBossTiles, "light_basalt.png",false, false);

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
						java.lang.System.out.println("TILES CHANGED" + mapComp.getTile(fBossTiles[0].x, fBossTiles[0].y));
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
				java.lang.System.out.println("DEFEATED SET");
				enComp.defeated = true;
			}
		});

		return msg;  // Return dialogue message for defeated enemy
	}





}

