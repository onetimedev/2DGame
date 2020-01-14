package scc210game.events;

import scc210game.ecs.Entity;

import java.util.List;

/**
 * Fired when the mouse cursor goes over an entity
 */
public class EntityHoverStopEvent implements UiEvent, ContainsEntities {
    public final Entity entity;

    public EntityHoverStopEvent(Entity entity) {
        this.entity = entity;
    }

    @Override
    public List<Entity> getEntities() {
        return List.of(this.entity);
    }
}
