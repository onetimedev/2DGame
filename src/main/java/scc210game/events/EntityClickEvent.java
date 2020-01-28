package scc210game.events;

import scc210game.ecs.Entity;

import java.util.List;

/**
 * Fired when an entity is clicked
 */
public class EntityClickEvent extends UiEvent implements ContainsEntities {
    public final float x;
    public final float y;
    public final Entity entity;

    public EntityClickEvent(float x, float y, Entity entity) {
        this.x = x;
        this.y = y;
        this.entity = entity;
    }

    @Override
    public List<Entity> getEntities() {
        return List.of(this.entity);
    }
}