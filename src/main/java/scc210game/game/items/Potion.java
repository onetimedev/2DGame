package scc210game.game.items;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Copyable;

public class Potion extends ItemData implements Copyable<Potion> {
    @Override
    public Jsonable serialize() {
        return new JsonObject();
    }

    @Override
    public Potion copy() {
        return this;
    }

    @Override
    public String infoData() {
        return "potion";
    }
}
