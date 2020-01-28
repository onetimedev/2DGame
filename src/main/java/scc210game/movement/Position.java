package scc210game.movement;

import scc210game.ecs.Component;

public class Position extends Component {

	public int xPos;
	public int yPos;

	public Position(int x, int y) {
		xPos = x;
		yPos = y;
	}


	@Override
	public String serialize() {
		return null;
	}
}
