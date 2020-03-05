package scc210game.game.resources;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Resource;
import scc210game.game.utils.LoadJsonNum;

import java.math.BigDecimal;
import java.util.Map;

public class ItemIDCounterResource extends Resource {
    private int lastItemID;

    public ItemIDCounterResource() {
        this.lastItemID = 0;
    }

    ItemIDCounterResource(int lastItemID) {
        this.lastItemID = lastItemID;
    }

    public int getItemID() {
        return lastItemID++;
    }

    static {
        register(ItemIDCounterResource.class, j -> {
            var json = (JsonObject) j;
            return new ItemIDCounterResource(LoadJsonNum.loadInt(json.get("lastItemID")));
        });
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("lastItemID", this.lastItemID));
    }
}
