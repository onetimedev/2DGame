package scc210game.game.components;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;

import java.util.function.BiConsumer;

public class Dialogue extends Component {
    /**
     * Message to display
     */
    public final String text;

    /**
     * Callback when enter pressed
     */
    public final BiConsumer<Entity, World> accept;

    /**
     * Callback when q pressed
     */
    public final BiConsumer<Entity, World> ignore;

    public Dialogue(String text, BiConsumer<Entity, World> accept, BiConsumer<Entity, World> ignore) {
        this.text = text;
        this.accept = accept;
        this.ignore = ignore;
    }

    @Override
    public String serialize() {
        return null;
    }
}
