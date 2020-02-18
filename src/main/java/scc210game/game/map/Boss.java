package scc210game.game.map;

import scc210game.engine.ecs.Component;

public class Boss extends Component {

	static {
		register(Boss.class, S->new Boss());
	}

	@Override
	public String serialize() {
		return "";
	}
}
