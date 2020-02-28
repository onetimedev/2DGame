package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class NPC extends Component {

    static {
        register(NPC.class, s -> new NPC());
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject();
    }
}
