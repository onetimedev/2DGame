package scc210game.game.map;

import scc210game.engine.ecs.Component;

/**
 * Component for locking / unlocking the players movement.
 */
public class PlayerLocked extends Component {

	public boolean locked;

	public PlayerLocked(boolean b) {
		locked = b;
	}

	@Override
	public String serialize() {
		return null;
	}
}

