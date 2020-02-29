package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class FinalBoss extends Component {

	static {
		register(FinalBoss.class, s->new FinalBoss());
	}

	@Override
	public Jsonable serialize() {
		return new JsonObject();
	}
}
