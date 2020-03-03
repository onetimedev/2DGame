package scc210game.game.systems;


import scc210game.engine.ecs.ECS;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.events.LeaveCombatEvent;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.game.components.Dialogue;
import scc210game.game.events.DialogueCreateEvent;

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
		java.lang.System.out.println("Left combat in map");

	}
}
