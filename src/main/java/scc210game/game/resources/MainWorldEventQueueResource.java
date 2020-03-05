package scc210game.game.resources;

import scc210game.engine.ecs.Resource;
import scc210game.engine.events.EventQueue;


public class MainWorldEventQueueResource extends Resource {

	public final EventQueue queue;

	public MainWorldEventQueueResource(EventQueue queue) {
		this.queue = queue;
	}

	@Override
	public boolean shouldKeep() {
		return false;
	}


}
