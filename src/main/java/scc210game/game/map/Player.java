package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class Player extends Component {

    static {
        register(Player.class, s->new Player());
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject();
    }
}
