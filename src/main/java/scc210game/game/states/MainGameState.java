package scc210game.game.states;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.ui.spawners.ClickableTextBoxSpawner;
import scc210game.engine.ui.spawners.DialogueSpawner;
import scc210game.engine.ui.spawners.DraggableBoxSpawner;
import scc210game.engine.ui.spawners.DroppableBoxSpawner;
import scc210game.game.components.Health;
import scc210game.game.components.Player;
import scc210game.game.spawners.PlayerSpawner;
import scc210game.game.spawners.ui.HealthBarSpawner;

public class MainGameState extends BaseInGameState {
	@Override
	public void onStart(World world) {
		world.entityBuilder().with(new DialogueSpawner("Test")).build();
		world.entityBuilder().with(new DraggableBoxSpawner(0, 0, 0.1f, 0.1f)).build();
		world.entityBuilder().with(new DroppableBoxSpawner(0.5f, 0.1f, 0.15f, 0.15f)).build();
		world.entityBuilder().with(new DroppableBoxSpawner(0.5f, 0.27f, 0.15f, 0.15f)).build();
		world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.1f, 0.07f, 0.07f, "click me", (Entity e, World w) -> {
			var trans = w.fetchComponent(e, UITransform.class);
			trans.xPos += 0.01;

			var player = w.applyQuery(Query.builder().require(Player.class).build()).findFirst().orElseThrow();
			var health = w.fetchComponent(player, Health.class);
			health.health -= 1;
		})).build();
		var player = world.entityBuilder().with(new PlayerSpawner()).build();
		world.entityBuilder().with(new HealthBarSpawner(0.025f, 0.01f, 0.95f, 0.01f, player)).build();
	}
}
