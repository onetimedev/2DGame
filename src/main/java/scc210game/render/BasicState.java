package scc210game.render;

import scc210game.ecs.World;
import scc210game.state.InputHandlingState;
import scc210game.ui.spawners.DialogueSpawner;

public class BasicState implements InputHandlingState {
	@Override
	public void onStart(World world) {
		world.entityBuilder().with(new DialogueSpawner("Test")).build();
	}
}
