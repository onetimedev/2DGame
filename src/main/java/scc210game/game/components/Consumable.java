package scc210game.game.components;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;

import java.util.function.BiConsumer;

public class Consumable extends Component {

    public BiConsumer<Entity, World> consume;

    public Consumable(BiConsumer<Entity, World> consume) {
        this.consume = consume;
    }

    @Override
    public String serialize() {
        return null;
    }
}
