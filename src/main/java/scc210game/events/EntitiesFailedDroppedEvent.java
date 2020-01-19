package scc210game.events;

import scc210game.ecs.Entity;

import java.util.List;

/**
 * Fired when an entity was dragged but the mouse was not released on another entity
 */
public class EntitiesFailedDroppedEvent implements UiEvent, ContainsEntities {
    public final Entity dropped;

    public EntitiesFailedDroppedEvent(Entity dropped) {
        this.dropped = dropped;
    }

    @Override
    public List<Entity> getEntities() {
        return List.of(this.dropped);
    }
}
