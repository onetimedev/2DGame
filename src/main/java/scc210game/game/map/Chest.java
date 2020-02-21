package scc210game.game.map;

import scc210game.engine.ecs.Component;

public class Chest extends Component {

	static {
		register(Chest.class, S->new Chest());
	}

	@Override
	public String serialize() {
		return "";
	}

}
