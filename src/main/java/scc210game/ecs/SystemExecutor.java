package scc210game.ecs;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * An executor for ECS Systems
 */
class SystemExecutor {
    private final List<? extends System> systems;

    public SystemExecutor(List<? extends System> systems) {
        this.systems = systems;
    }

    /**
     * Perform one round of systems execution
     *
     * @param world the {@link World} to run
     */
    public void runOnce(@Nonnull World world) {
        for (final System s : this.systems) {
            s.run(world);
        }
    }
}
