package scc210game.game.states;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.state.InputHandlingState;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.ui.spawners.ClickableTextBoxSpawner;
import scc210game.engine.ui.spawners.DialogueSpawner;
import scc210game.engine.ui.spawners.DraggableBoxSpawner;
import scc210game.engine.ui.spawners.DroppableBoxSpawner;

public class MainGameState extends InputHandlingState {
	@Override
	public void onStart(World world) {
		world.entityBuilder().with(new DialogueSpawner("Test")).build();
		world.entityBuilder().with(new DraggableBoxSpawner(0, 0, 0.1f, 0.1f)).build();
		world.entityBuilder().with(new DroppableBoxSpawner(0.5f, 0.1f, 0.15f, 0.15f)).build();
		world.entityBuilder().with(new DroppableBoxSpawner(0.5f, 0.27f, 0.15f, 0.15f)).build();
		world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.1f, 0.07f, 0.07f, "click me", (Entity e, World w) -> {
			var trans = w.fetchComponent(e, UITransform.class);
			trans.xPos += 0.01;
		})).build();
	}
}
