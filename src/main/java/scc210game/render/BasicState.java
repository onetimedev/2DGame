package scc210game.render;

import scc210game.ecs.World;
import scc210game.state.InputHandlingState;
import scc210game.ui.spawners.DialogueSpawner;
import scc210game.ui.spawners.DraggableBoxSpawner;
import scc210game.ui.spawners.DroppableBoxSpawner;

public class BasicState implements InputHandlingState {
	@Override
	public void onStart(World world) {
		world.entityBuilder().with(new DialogueSpawner("Test")).build();
		world.entityBuilder().with(new DraggableBoxSpawner(0, 0, 0.1f, 0.1f)).build();
		world.entityBuilder().with(new DroppableBoxSpawner(0.5f, 0.1f, 0.15f, 0.15f)).build();
		world.entityBuilder().with(new DroppableBoxSpawner(0.5f, 0.27f, 0.15f, 0.15f)).build();
	}
}
