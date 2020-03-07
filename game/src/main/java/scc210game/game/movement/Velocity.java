package scc210game.game.movement;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.game.utils.LoadJsonNum;

public class Velocity extends Component {

	public float dx;
	public float dy;


	static {
		register(Velocity.class, j -> {
			var json = (JsonObject) j;
			float velX = LoadJsonNum.loadFloat(json.get("dx"));
			float velY = LoadJsonNum.loadFloat(json.get("dy"));

			return new Velocity(velX, velY);
		});
	}


	public Velocity(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public Jsonable serialize() {
		final Jsonable json = new JsonObject() {{
			this.put("dx", Velocity.this.dx);
			this.put("dy", Velocity.this.dy);
		}};

		return json;
	}



}
