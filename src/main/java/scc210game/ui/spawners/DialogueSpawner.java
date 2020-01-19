package scc210game.ui.spawners;

import scc210game.ecs.Spawner;
import scc210game.ecs.World;
import scc210game.ui.UIText;
import scc210game.ui.UITransform;

public class DialogueSpawner implements Spawner {
    private final String message;

    public DialogueSpawner(String message) {
        this.message = message;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        return builder
                .with(new UITransform(0, 0, 0, 1.0f, 0.2f))
                .with(new UIText(this.message));
    }
}

