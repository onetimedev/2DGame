package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

import java.util.Map;

public class Chest extends Component {
    public final Tile tile;

    static {
        // TODO
        register(Chest.class, s -> new Chest(null));
    }

    public Chest(Tile tile) {
        this.tile = tile;
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("tile", this.tile.serialize()));
    }

}
