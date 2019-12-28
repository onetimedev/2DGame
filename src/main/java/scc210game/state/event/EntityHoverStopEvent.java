package scc210game.state.event;

import scc210game.ecs.Entity;

/**
 * Fired when the mouse cursor goes over an entity
 */
public class EntityHoverStopEvent implements UiEvent {
    public final Entity entity;

    public EntityHoverStopEvent(Entity entity) {
        this.entity = entity;
    }
}
