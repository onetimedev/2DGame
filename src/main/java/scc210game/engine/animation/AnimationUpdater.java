package scc210game.engine.animation;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

public class AnimationUpdater implements System {
    private static final Query q = Query.builder().require(Animate.class).build();

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        var entities = world.applyQuery(q);

        for (Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next();

            var animation = world.fetchComponent(entity, Animate.class);
            animation.update(timeDelta);

            if (animation.isComplete()) {
                animation.completionCallback.accept(entity, world);
            }
        }
    }
}
