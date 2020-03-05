package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.game.utils.LoadJsonNum;

public class OldPosition extends Component {
    public float xPos;
    public float yPos;

    static {
        register(OldPosition.class, j -> {
            var json = (JsonObject) j;
            float x = LoadJsonNum.loadFloat(json.get("xPos"));
            float y = LoadJsonNum.loadFloat(json.get("yPos"));

            return new OldPosition(x, y);
        });
    }

    public OldPosition(float x, float y) {
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public Jsonable serialize() {
        final Jsonable json = new JsonObject() {{
            this.put("xPos", OldPosition.this.xPos);
            this.put("yPos", OldPosition.this.yPos);
        }};

        return json;
    }
}
