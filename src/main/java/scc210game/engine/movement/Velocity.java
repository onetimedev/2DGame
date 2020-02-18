package scc210game.engine.movement;

import scc210game.engine.ecs.Component;

public class Velocity extends Component {

	public float dx;
	public float dy;

	public Velocity(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public String serialize() {
		return null;
	}



}
