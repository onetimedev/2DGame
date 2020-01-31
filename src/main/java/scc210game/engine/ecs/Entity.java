package scc210game.engine.ecs;

import javax.annotation.Nonnull;
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

    @Nonnull
    public static Entity make() {
        return EntityAllocator.allocate();
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
