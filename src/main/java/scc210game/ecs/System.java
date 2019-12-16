package scc210game.ecs;

import javax.annotation.Nonnull;
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

    public void run(World world) {
        final Query q = this.getQuery();
        final Stream<Entity> entities = world.applyQuery(q);

        entities.forEach(e -> this.actOnEntity(e, world));
    }

    /**
     * Process an entity in the world.
     *
     * @param e The entity to process
     */
    public abstract void actOnEntity(Entity e, World world);
}
