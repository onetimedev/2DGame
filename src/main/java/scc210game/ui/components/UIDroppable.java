package scc210game.ui.components;

import scc210game.ecs.Component;
import scc210game.ecs.Entity;
import scc210game.ecs.World;
import scc210game.utils.TriConsumer;

/**
 * Component that flags entities that can have dragged entities dropped onto them
 */
public class UIDroppable extends Component {
    static {
        register(UIDroppable.class, s -> new UIInteractive());
    }

    /**
     * The function to call to accept an entity being dropped onto this entity
     * <p>
     * The first parameter is the entity the component is attached to.
     * The second parameter is the entity that was dropped.
     * The last parameter is the current World.
     * <p>
     * Example:
     * <pre>
     *     {@code
     *     (Entity thisEntity, Entity droppedEntity, World w) -> {
     *         // ...
     *     }
     *     }
     * </pre>
     */
    public final TriConsumer<Entity, Entity, World> acceptor;

    public UIDroppable(TriConsumer<Entity, Entity, World> acceptor) {
        this.acceptor = acceptor;
    }


    @Override
    public String serialize() {
        return "";
    }
}
