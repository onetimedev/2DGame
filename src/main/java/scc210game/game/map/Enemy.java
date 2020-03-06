package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.game.utils.LoadJsonNum;

import java.util.Map;

public class Enemy extends Component {

    public boolean defeated = false;
    public int damage = 1;
    public int id;
    static {
        register(Enemy.class, j -> {
            var json = (JsonObject) j;
            return new Enemy((Boolean) json.get("defeated"), LoadJsonNum.loadInt(json.get("damage")), LoadJsonNum.loadInt(json.get("id")));
        });
    }


    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("defeated", this.defeated, "damage", this.damage, "id", this.id));
    }


    public Enemy(boolean defeat, int damage, int id) {
        this.defeated = defeat;
        this.id = id;
        this.damage = damage;
    }
}
