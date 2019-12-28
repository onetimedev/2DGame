package scc210game.state.event;

import scc210game.ecs.Entity;

/**
 * Fired when an entity was dragged and then dropped onto another
 */
public class EntityDroppedEvent {
    public final Entity dropped;
    public final Entity droppedOnto;

    public EntityDroppedEvent(Entity dropped, Entity droppedOnto) {
        this.dropped = dropped;
        this.droppedOnto = droppedOnto;
    }
}
