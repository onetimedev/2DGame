package scc210game.engine.ecs;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * A class for allocating entities.
 * <p>
 * Currently this just hands out incremental entity IDs
 * In the future we could increase the amount of possible
 * entities performance by re-using entity IDs
 */
class EntityAllocator extends SerDe {
    private long currentID;

    EntityAllocator() {
        this.currentID = 0;
    }

    /**
     * Allocate a new entity.
     *
     * @return The newly allocated entity
     */
    @Nonnull
    Entity allocate() {
        long id = this.currentID++;

        return new Entity(id);
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("currentID", this.currentID));
    }
}
