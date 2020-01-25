package scc210game.events;

import scc210game.ecs.Entity;

import java.util.List;

/**
 * Fired when an entity is dragged
 */
public class EntitiesDraggedEvent extends UiEvent implements ContainsEntities {
    public final Entity entity;

    /**
     * Origin x coordinate of the drag
     */
    public final float originX;

    /**
     * Origin y coordinate of the drag
     */
    public final float originY;

    /**
     * Change in x coordinate since the last drag event
     */
    public final float dx;

    /**
     * Change in y coordinate since the last drag event
     */
    public final float dy;

    public EntitiesDraggedEvent(Entity entity, float originX, float originY, float dx, float dy) {
        this.entity = entity;
        this.originX = originX;
        this.originY = originY;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public List<Entity> getEntities() {
        return List.of(this.entity);
    }
}
