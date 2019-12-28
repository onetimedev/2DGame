package scc210game.state.event;

import scc210game.ecs.Entity;

/**
 * Fired when an entity is dragged
 * <p>
 * dy/dx are the offsets from the position the entity was at at the start of the drag
 */
public class EntityDraggedEvent {
    public final Entity entity;

    public final int dx;
    public final int dy;

    public EntityDraggedEvent(Entity entity, int dx, int dy) {
        this.entity = entity;
        this.dx = dx;
        this.dy = dy;
    }
}
