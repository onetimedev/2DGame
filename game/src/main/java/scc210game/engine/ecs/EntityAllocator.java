package scc210game.engine.ecs;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.game.utils.LoadJsonNum;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * A class for allocating entities.
 * <p>
 * Currently this just hands out incremental entity IDs
 * In the future we could increase the amount of possible
 * entities performance by re-using entity IDs
 */
class EntityAllocator {
    private long currentID;

    EntityAllocator() {
        this.currentID = 0;
    }

    EntityAllocator(long initialID) {
        this.currentID = initialID;
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

    public Jsonable serialize() {
        return new JsonObject(Map.of("currentID", this.currentID));
    }

    public static EntityAllocator deserialize(Jsonable j) {
        var json = (JsonObject) j;
        return new EntityAllocator(LoadJsonNum.loadLong(json.get("currentID")));
    }
}
