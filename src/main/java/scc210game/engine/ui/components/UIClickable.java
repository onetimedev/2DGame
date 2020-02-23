package scc210game.engine.ui.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.utils.SerializableBiConsumer;
import scc210game.engine.utils.SerializeToBase64;

import java.util.Map;

/**
 * Component that flags entities that can be clicked
 */
public class UIClickable extends Component {
    static {
        register(UIClickable.class, s -> new UIInteractive());
    }

    /**
     * The function to call to accept this entity being clicked
     * <p>
     * The first parameter is the entity the component is attached to.
     * The last parameter is the current World.
     * <p>
     * Example:
     * <pre>
     *     {@code
     *     (Entity thisEntity, World w) -> {
     *         // ...
     *     }
     *     }
     * </pre>
     */
    public final SerializableBiConsumer<Entity, World> acceptor;

    public UIClickable(SerializableBiConsumer<Entity, World> acceptor) {
        this.acceptor = acceptor;
    }


    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("acceptor", SerializeToBase64.serializeToBase64(this.acceptor)));
    }
}
