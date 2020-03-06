package scc210game.engine.events;

import scc210game.engine.ecs.Entity;

import java.util.List;

/**
 * Fired when the mouse cursor goes over an entity
 */
public class EntityHoverStartEvent extends UiEvent implements ContainsEntities {
    public final Entity entity;

    public EntityHoverStartEvent(Entity entity) {
        this.entity = entity;
    }

    @Override
    public List<Entity> getEntities() {
        return List.of(this.entity);
    }
}
