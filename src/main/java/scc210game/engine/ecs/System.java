package scc210game.engine.ecs;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * Systems in the ECS model
 */
public interface System {
    void run(@Nonnull World world, @Nonnull Duration timeDelta);
}
