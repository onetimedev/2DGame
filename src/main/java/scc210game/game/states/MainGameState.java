package scc210game.game.states;

import scc210game.engine.ecs.World;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPush;
import scc210game.engine.state.trans.Transition;
import scc210game.game.spawners.MapSpawner;
import scc210game.game.spawners.PlayerSpawner;
import scc210game.game.spawners.ui.EnterInventoryButtonSpawner;
import scc210game.game.states.events.EnterInventoryEvent;
import scc210game.game.states.events.EnterTwoInventoryEvent;

public class MainGameState extends BaseInGameState {
	@Override
	public void onStart(World world) {
		world.entityBuilder().with(new MapSpawner()).build();
		world.entityBuilder().with(new PlayerSpawner()).build();
		world.entityBuilder().with(
				new EnterInventoryButtonSpawner(0, 0, 0.05f, 0.05f))
				.build();
	}

	@Override
	public Transition handleEvent(StateEvent evt, World world) {
		if (evt instanceof EnterInventoryEvent) {
			EnterInventoryEvent evt1 = (EnterInventoryEvent) evt;
			return new TransPush(new InventoryViewState(world, evt1.invEnt, evt1.inv));
		}

		if (evt instanceof EnterTwoInventoryEvent) {
			EnterTwoInventoryEvent evt1 = (EnterTwoInventoryEvent) evt;
			return new TransPush(new TwoInventoryViewState(world, evt1.inv0Ent, evt1.inv0, evt1.inv1Ent, evt1.inv1));
		}

		return super.handleEvent(evt, world);
	}
}
