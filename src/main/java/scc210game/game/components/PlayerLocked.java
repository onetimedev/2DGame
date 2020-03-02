package scc210game.game.components;

import com.github.cliftonlabs.json_simple.Jsonable;
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
	public Jsonable serialize() {
		return null;
	}
}

