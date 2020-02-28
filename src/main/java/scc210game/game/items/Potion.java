package scc210game.game.items;

import scc210game.engine.ecs.Copyable;

public class Potion extends ItemData implements Copyable<Potion> {
    @Override
    public String serialize() {
        return null;
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
