package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

import java.util.Map;

/**
 * Component for locking / unlocking the players movement.
 */
public class PlayerLocked extends Component {
	static {
		register(PlayerLocked.class, j -> {
			var json = (JsonObject) j;
			return new PlayerLocked((Boolean) json.get("locked"));
		});
	}

	public boolean locked;

	public PlayerLocked(boolean b) {
		locked = b;
	}

	@Override
	public Jsonable serialize() {
		return new JsonObject(Map.of("locked", this.locked));
	}
}

