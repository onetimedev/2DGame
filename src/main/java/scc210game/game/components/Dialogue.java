package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.utils.SerDeBase64;
import scc210game.engine.utils.SerializableBiConsumer;

import java.util.Map;

public class Dialogue extends Component {
    static {
        register(Dialogue.class, j -> {
            var json = (JsonObject) j;

            var text = (String) json.get("text");

            @SuppressWarnings("unchecked")
            var accept = SerDeBase64.deserializeFromBase64((String) json.get("accept"),
                    (Class<SerializableBiConsumer<Entity, World>>)(Class<?>)SerializableBiConsumer.class);

            @SuppressWarnings("unchecked")
            var ignore = SerDeBase64.deserializeFromBase64((String) json.get("ignore"),
                    (Class<SerializableBiConsumer<Entity, World>>)(Class<?>)SerializableBiConsumer.class);

            return new Dialogue(text, accept, ignore);
        });
    }

    /**
     * Message to display
     */
    public final String text;

    /**
     * Callback when enter pressed
     */
    public final SerializableBiConsumer<Entity, World> accept;

    /**
     * Callback when q pressed
     */
    public final SerializableBiConsumer<Entity, World> ignore;

    public Dialogue(String text, SerializableBiConsumer<Entity, World> accept, SerializableBiConsumer<Entity, World> ignore) {
        this.text = text;
        this.accept = accept;
        this.ignore = ignore;
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of(
                "text", this.text,
                "accept", SerDeBase64.serializeToBase64(this.accept),
                "ignore", SerDeBase64.serializeToBase64(this.ignore)));
    }
}
