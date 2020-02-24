package scc210game.engine.ecs;

import javax.annotation.Nullable;

/**
 * Represents an entity in the entity-component-system model
 */
public class Entity {
    // private since the internal representation could change in the future
    private final long id;

    // ctor is package-private, entities can only be obtained by the allocator
    Entity(long id) {
        this.id = id;
    }

    /**
     * get the ID of the entity, don't do this unless you know what you're doing
     *
     * @return the ID of the entity
     */
    public long unsafeGetID() {
        return this.id;
    }

    /**
     * construct an entity unsafely, don't do this unless you know what you're doing
     * you have no way to know if the entity ID is already used or not.
     *
     * @param id the ID of the entity to create
     * @return an entity with the given ID
     */
    public Entity unsafeMakeEntity(long id) {
        return new Entity(id);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return this.id == entity.id;
    }

    @Override
    public int hashCode() {
        return (int) (this.id ^ (this.id >>> 32));
    }
}
