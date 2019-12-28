package scc210game.state.event;

import scc210game.ecs.Entity;

/**
 * Fired when an entity is clicked
 */
public class EntityClickEvent implements UiEvent {
    public final Entity entity;

    public EntityClickEvent(Entity entity) {
        this.entity = entity;
    }
}
