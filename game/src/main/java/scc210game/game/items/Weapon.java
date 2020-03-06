package scc210game.game.items;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Copyable;
import scc210game.game.utils.LoadJsonNum;

import java.util.Map;

public class Weapon extends ItemData implements Copyable<Weapon> {
    public final int damage;
    public final Element element;

    public Weapon(int damage, Element element) {
        this.damage = damage;
        this.element = element;
    }

    static {
        register(Weapon.class, j -> {
            var json = (JsonObject) j;
            var damage = LoadJsonNum.loadInt(json.get("damage"));
            var element = Element.valueOf((String) json.get("element"));

            return new Weapon(damage, element);
        });
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of(
                "damage", this.damage,
                "element", this.element.toString()
        ));
    }

    @Override
    public String infoData() {
        return "damage: " + this.damage + "\nelement: " + this.element.name;
    }

    @Override
    public Weapon copy() {
        return this;
    }
}
