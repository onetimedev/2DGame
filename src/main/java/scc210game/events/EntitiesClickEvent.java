package scc210game.events;

import scc210game.ecs.Entity;

import java.util.List;

/**
 * Fired when an entity is clicked
 */
public class EntitiesClickEvent implements UiEvent, ContainsEntities {
    public final Entity entity;

    public EntitiesClickEvent(Entity entity) {
        this.entity = entity;
    }

    @Override
    public List<Entity> getEntities() {
        return List.of(this.entity);
    }
}
