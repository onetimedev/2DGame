package scc210game.combat;

import scc210game.ecs.Component;

public class Health extends Component {

	public int healthVal;

	public Health(int h) {
		healthVal = h;
	}


	@Override
	public String serialize() {
		return null;
	}
}
