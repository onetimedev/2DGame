package scc210game.ecs;

import scc210game.state.State;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * An executor for ECS Systems
 */
class SystemExecutor {
    private final List<? extends System> systems;
    private Instant lastRun;

    public SystemExecutor(List<? extends System> systems) {
        this.systems = systems;
        this.lastRun = Instant.now();
    }

    /**
     * Perform one round of systems execution
     *
     * @param world the {@link World} to run
     */
    public void runOnce(@Nonnull World world, @Nonnull Class<? extends State> currentState) {
        Instant now = Instant.now();
        Duration delta = Duration.between(this.lastRun, now);

        for (final System s : this.systems) {
            s.run(world, currentState, delta);
        }

        this.lastRun = now;
    }
}
