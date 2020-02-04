package scc210game.engine.ui.components;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;

import java.util.function.BiConsumer;

/**
 * Component that flags entities that can be clicked
 */
public class UIClickable extends Component {
    static {
        register(UIClickable.class, s -> new UIInteractive());
    }

    /**
     * The function to call to accept this entity being clicked
     * <p>
     * The first parameter is the entity the component is attached to.
     * The last parameter is the current World.
     * <p>
     * Example:
     * <pre>
     *     {@code
     *     (Entity thisEntity, World w) -> {
     *         // ...
     *     }
     *     }
     * </pre>
     */
    public final BiConsumer<Entity, World> acceptor;

    public UIClickable(BiConsumer<Entity, World> acceptor) {
        this.acceptor = acceptor;
    }


    @Override
    public String serialize() {
        return "";
    }
}
