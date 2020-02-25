package scc210game.game.items;

import scc210game.engine.ecs.Copyable;

public class Weapon extends ItemData implements Copyable<Weapon> {
    public final int damage;
    public final String lore;
    public final Element element;

    public Weapon(int damage, String lore, Element element) {
        this.damage = damage;
        this.lore = lore;
        this.element = element;
    }

    @Override
    public String serialize() {
        return null;
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
