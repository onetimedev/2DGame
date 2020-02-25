package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import scc210game.engine.ecs.Component;

public class Enemy extends Component {

    public boolean defeated = false;

    static {
        register(Enemy.class, s -> {
            final JsonObject json = Jsoner.deserialize(s, new JsonObject());
            boolean defeat;
            defeat = s.equals("true");

            return new Enemy(defeat);
        });
    }


    @Override
    public String serialize() {
        return Boolean.toString(this.defeated);
    }


    public Enemy(boolean defeat) {
        this.defeated = defeat;
    }
}
