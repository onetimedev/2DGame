package scc210game.engine.ui.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

/**
 * Component that flags entities that can be dragged using the mouse
 */
public class UIDraggable extends Component {
    static {
        register(UIDraggable.class, s -> new UIDraggable());
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject();
    }
}
