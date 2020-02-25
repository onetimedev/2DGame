package scc210game.engine.movement;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import scc210game.engine.ecs.Component;

public class Velocity extends Component {

    public float dx;
    public float dy;


    static {
        register(Velocity.class, s -> {
            final JsonObject json = Jsoner.deserialize(s, new JsonObject());
            float velX = (float) json.get("dx");
            float velY = (float) json.get("dy");

            return new Velocity(velX, velY);
        });
    }


    public Velocity(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public String serialize() {
        final Jsonable json = new JsonObject() {{
            this.put("dx", Velocity.this.dx);
            this.put("dy", Velocity.this.dy);
        }};

        return json.toJson();
    }


}
