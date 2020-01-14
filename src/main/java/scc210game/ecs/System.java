package scc210game.ecs;

import scc210game.state.State;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * Systems in the ECS model
 */
public interface System {
    void run(@Nonnull World world, @Nonnull Class<? extends State> currentState, @Nonnull Duration timeDelta);
}
