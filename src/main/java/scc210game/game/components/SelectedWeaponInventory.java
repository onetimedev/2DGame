package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class SelectedWeaponInventory extends Component {
    static {
        register(SelectedWeaponInventory.class, s -> new SelectedWeaponInventory());
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject();
    }
}
