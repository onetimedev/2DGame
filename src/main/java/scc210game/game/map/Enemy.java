package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import scc210game.engine.ecs.Component;

public class Enemy extends Component {

    public boolean defeated = false;

    static {
        register(Enemy.class, s-> {
          final JsonObject json = Jsoner.deserialize(s, new JsonObject());
          boolean defeat;
          if(s.equals("true"))
            defeat = true;
          else
            defeat = false;

          return new Enemy(defeat);
        });
    }


    @Override
    public String serialize() {
        return Boolean.toString(defeated);
    }


    public Enemy(boolean defeat) {
        defeated = defeat;
    }
}
