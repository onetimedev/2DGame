package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class Chest extends Component {

	static {
		register(Chest.class, S->new Chest());
	}

	@Override
	public Jsonable serialize() {
		return new JsonObject();
	}

}
