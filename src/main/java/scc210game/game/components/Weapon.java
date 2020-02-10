package scc210game.game.components;

import scc210game.engine.ecs.Component;

public class Weapon extends Component {
    public final int damage;
    public final String name;
    public final String lore;

    public Weapon(int damage, String name, String lore) {
        this.damage = damage;
        this.name = name;
        this.lore = lore;
    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public Component clone() {
        return this;
    }
}
