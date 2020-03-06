package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import org.jsfml.graphics.RenderWindow;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.utils.SerDeBase64;
import scc210game.engine.utils.SerializableBiConsumer;
import scc210game.engine.utils.SerializableTriConsumer;

import java.util.Map;

public class Consumable extends Component {
    public SerializableBiConsumer<Entity, World> consume;

    public Consumable(SerializableBiConsumer<Entity, World> consume) {
        this.consume = consume;
    }

    static {
        register(Consumable.class, j -> {
            var json = (JsonObject)	j;

            @SuppressWarnings("unchecked")
            var consume = SerDeBase64.deserializeFromBase64((String) json.get("consume"),
                    (Class<SerializableBiConsumer<Entity, World>>)(Class<?>)SerializableBiConsumer.class);

            return new Consumable(consume);
        });
    }

    public Jsonable serialize() {
        return new JsonObject(Map.of(
                "consume", SerDeBase64.serializeToBase64(this.consume)));
    }
}
