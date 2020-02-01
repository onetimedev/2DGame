package scc210game.engine.ecs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A class for allocating entities.
 * <p>
 * Currently this just hands out incremental entity IDs
 * In the future we could increase the amount of possible
 * entities performance by re-using entity IDs
 */
class EntityAllocator {
    private long currentID;

    @Nullable
    private static EntityAllocator instance = null;

    private EntityAllocator() {
        this.currentID = 0;
    }

    @Nonnull
    private Entity allocateInner() {
        long id = this.currentID++;

        return new Entity(id);
    }

    /**
     * Allocate a new entity.
     *
     * @return The newly allocated entity
     */
    @Nonnull
    static Entity allocate() {
        return getInstance().allocateInner();
    }

    @Nonnull
    static EntityAllocator getInstance() {
        if (EntityAllocator.instance == null)
            EntityAllocator.instance = new EntityAllocator();

        return EntityAllocator.instance;
    }
}
