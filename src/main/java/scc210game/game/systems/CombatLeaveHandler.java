package scc210game.game.systems;


import scc210game.engine.combat.Scoring;
import scc210game.engine.ecs.ECS;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.events.LeaveCombatEvent;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.game.components.Dialogue;
import scc210game.game.components.PlayerLocked;
import scc210game.game.events.DialogueCreateEvent;
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
		scoring.playerExperience = evt.score.playerExperience;


	}
}
