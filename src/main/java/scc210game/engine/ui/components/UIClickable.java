package scc210game.engine.ui.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.utils.SerDeBase64;
import scc210game.engine.utils.SerializableBiConsumer;

import java.util.Map;

/**
 * Component that flags entities that can be clicked
 */
public class UIClickable extends Component {
    static {
        register(UIClickable.class, j -> {
            var json = (JsonObject) j;

            @SuppressWarnings("unchecked")
            var acceptor = SerDeBase64.deserializeFromBase64((String) json.get("acceptor"),
                    (Class<SerializableBiConsumer<Entity, World>>)(Class<?>)SerializableBiConsumer.class);

            return new UIClickable(acceptor);
        });
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
        return new JsonObject(Map.of("acceptor", SerDeBase64.serializeToBase64(this.acceptor)));
    }
}
