package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class Boss extends Component {

	static {
		register(Boss.class, s->new Boss());
	}

	@Override
	public Jsonable serialize() {
		return new JsonObject();
	}
}
