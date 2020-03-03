package scc210game.game.systems;


import scc210game.engine.combat.Scoring;
import scc210game.engine.ecs.*;
import scc210game.engine.ecs.System;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.events.LeaveCombatEvent;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.game.components.Dialogue;
import scc210game.game.components.PlayerLocked;
import scc210game.game.events.DialogueCreateEvent;
import scc210game.game.map.Map;
import scc210game.game.map.Player;

import javax.annotation.Nonnull;

import java.time.Duration;
import java.util.Iterator;

public class CombatLeaveHandler implements System {
	private final EventQueueReader eventReader;


	public CombatLeaveHandler(ECS ecs) {
		this.eventReader = ecs.eventQueue.makeReader();
		ecs.eventQueue.listen(this.eventReader, LeaveCombatEvent.class);
	}

	@Override
	public void run(@Nonnull World world, @Nonnull Duration timeDelta) {

		for (Iterator<Event> it = world.ecs.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
			Event e = it.next();
			this.handleEvent(world, e);
		}
	}

	/**
	 * Recieve data from combat system
	 * @param world
	 * @param e
	 */
	private void handleEvent(World world, Event e) {
		var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
		if (!playerEntO.isPresent())
			return;
		var player = playerEntO.get();

		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.zoom(1f/0.6f);
		var positionLocked = world.fetchComponent(player, PlayerLocked.class);
		positionLocked.locked = false;

		var scoring = world.fetchComponent(player, Scoring.class);

		LeaveCombatEvent evt = (LeaveCombatEvent) e;

		java.lang.System.out.println("Scoring: " + scoring);
		java.lang.System.out.println("Event: " + evt);
		java.lang.System.out.println("Evt Score: " + evt.score);
		scoring.playerExperience = evt.score.playerExperience;

		//evt.enemyw
		world.deactivateCombat();

		if(evt.playerWins) {
			enemyDefeated(evt.enemy, world);
			combatEndDialog(true);
		}
		else
			combatEndDialog(false);




	}

	private String combatEndDialog(boolean playerWin) {
		if(playerWin)
			java.lang.System.out.println("yup");

		return "";
	}


	private void enemyDefeated(Entity enemy, World world) {
		var mapEnt0 = world.applyQuery(Query.builder().require(Map.class).build()).findFirst();
		if (!mapEnt0.isPresent())
			return;
		var map = mapEnt0.get();
		var mapComp = world.fetchComponent(map, Map.class);





	}




}
