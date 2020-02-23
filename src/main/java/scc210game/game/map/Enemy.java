package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import scc210game.engine.ecs.Component;

import java.util.Map;

public class Enemy extends Component {

    public boolean defeated = false;

    static {
        register(Enemy.class, s-> {
          final JsonObject json = Jsoner.deserialize(s, new JsonObject());
            return new Enemy((Boolean) json.get("defeated"));
        });
    }


    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("defeated", this.defeated));
    }


    public Enemy(boolean defeat) {
        this.defeated = defeat;
    }
}
