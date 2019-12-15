package scc210game.ecs;

/**
 * A class for allocating entities.
 *
 * Currently this just hands out incremental entity IDs
 * In the future we could increase the amount of possible
 * entities performance by re-using entity IDs
 */
class EntityAllocator {
    private long currentID;

    private static EntityAllocator instance = null;

    private EntityAllocator() {
        this.currentID = 0;
    }

    private Entity allocateInner() {
        long id = this.currentID++;

        return new Entity(id);
    }

    /**
     * Allocate a new entity.
     * @return The newly allocated entity
     */
    public static Entity allocate() {
        return getInstance().allocateInner();
    }

    public static EntityAllocator getInstance() {
        if (EntityAllocator.instance == null)
            EntityAllocator.instance = new EntityAllocator();

        return EntityAllocator.instance;
    }
}
