package scc210game.engine.movement;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import scc210game.engine.ecs.Component;

public class Position extends Component {

	public float xPos;
	public float yPos;

	static {
		register(Position.class, s-> {
			final JsonObject json = Jsoner.deserialize(s, new JsonObject());
			float x = (float) json.get("xPos");
			float y = (float) json.get("yPos");

			return new Position(x, y);
		});
	}

	public Position(float x, float y) {
		xPos = x;
		yPos = y;
	}

	@Override
	public String serialize() {
		final Jsonable json = new JsonObject() {{
			this.put("xPos", Position.this.xPos);
			this.put("yPos", Position.this.yPos);
		}};

		return json.toJson();
	}

}
