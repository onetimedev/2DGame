package scc210game.game.items;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Copyable;
import scc210game.game.utils.LoadJsonNum;

import java.util.Map;

public class Weapon extends ItemData implements Copyable<Weapon> {
    public final int damage;
    public final String lore;
    public final Element element;

    public Weapon(int damage, String lore, Element element) {
        this.damage = damage;
        this.lore = lore;
        this.element = element;
    }

    static {
        register(Weapon.class, j -> {
            var json = (JsonObject) j;
            var damage = LoadJsonNum.loadInt(json.get("damage"));
            var lore = (String) json.get("lore");
            var element = Element.valueOf((String) json.get("element"));

            return new Weapon(damage, lore, element);
        });
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of(
                "damage", this.damage,
                "lore", this.lore,
                "element", this.element.toString()
        ));
    }

    @Override
    public String infoData() {
        return "damage: " + this.damage + "\nlore: " + this.lore;
    }

    @Override
    public Weapon copy() {
        return this;
    }
}
