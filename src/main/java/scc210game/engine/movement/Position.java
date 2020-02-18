package scc210game.engine.movement;

import scc210game.engine.ecs.Component;

public class Position extends Component {

	public float xPos;
	public float yPos;

	public Position(float x, float y) {
		xPos = x;
		yPos = y;
	}


	@Override
	public String serialize() {
		return null;
	}
}
