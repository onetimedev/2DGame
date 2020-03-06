package scc210game.game.resources;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Resource;

import java.util.Map;

public class ZoomStateResource extends Resource {
    public boolean zoomed;

    public ZoomStateResource(boolean zoomed) {
        this.zoomed = zoomed;
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("zoomed", this.zoomed));
    }

    static {
        register(ZoomStateResource.class, j -> {
            var json = (JsonObject) j;
            Boolean zoomed1 = (Boolean) json.get("zoomed");
            return new ZoomStateResource(zoomed1);
        });
    }
}
