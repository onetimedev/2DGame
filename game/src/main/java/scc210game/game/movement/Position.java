package scc210game.game.movement;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.game.utils.LoadJsonNum;

public class Position extends Component {

	public float xPos;
	public float yPos;

	static {
		register(Position.class, j -> {
            var json = (JsonObject) j;
			float x = LoadJsonNum.loadFloat(json.get("xPos"));
			float y = LoadJsonNum.loadFloat(json.get("yPos"));

			return new Position(x, y);
		});
	}

	public Position(float x, float y) {
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public Jsonable serialize() {
        final Jsonable json = new JsonObject() {{
            this.put("xPos", Position.this.xPos);
            this.put("yPos", Position.this.yPos);
        }};

        return json;
    }
}
