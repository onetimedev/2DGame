package scc210game.events;

import scc210game.ecs.Entity;

import java.util.List;

/**
 * Fired when an entity is dragged
 * <p>
 * dy/dx are the offsets from the position the entity was at at the start of the drag
 */
public class EntitiesDraggedEvent implements UiEvent, ContainsEntities {
    public final Entity entity;

    public final int dx;
    public final int dy;

    public EntitiesDraggedEvent(Entity entity, int dx, int dy) {
        this.entity = entity;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public List<Entity> getEntities() {
        return List.of(this.entity);
    }
}
