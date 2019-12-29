package scc210game.ecs;

import scc210game.state.State;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.stream.Stream;

/**
 * Systems in the ECS model
 */
public abstract class System {
    private static long idCounter = 0;
    private Query q;

    final long id;

    public System() {
        this.id = System.idCounter++;
    }

    public @Nonnull
    abstract Query getQuery();

    private Query getCachedQuery() {
        if (this.q == null)
            this.q = this.getQuery();

        return this.q;
    }

    public void run(@Nonnull World world, @Nonnull Class<? extends State> currentState, @Nonnull Duration timeDelta) {
        final Query q = this.getCachedQuery();
        final Stream<Entity> entities = world.applyQuery(q, currentState);

        entities.forEach(e -> {
            world.resetModifiedState(e);
            this.actOnEntity(e, world, timeDelta);
        });
    }

    /**
     * Process an entity in the world.
     *
     * @param e         The entity to process
     * @param world     The world the entity belongs to
     * @param timeDelta The time difference between now and the last run of this system
     */
    public abstract void actOnEntity(Entity e, @Nonnull World world, @Nonnull Duration timeDelta);
}
