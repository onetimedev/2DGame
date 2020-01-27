package scc210game.events;

import scc210game.ecs.Entity;

import java.util.List;

/**
 * Fired when an entity is dragged
 */
public class EntityDraggedEvent extends UiEvent implements ContainsEntities {
    public final Entity entity;

    /**
     * Origin x coordinate of the drag (in percentage of the screen)
     */
    public final float originX;

    /**
     * Origin y coordinate of the drag (in percentage of the screen)
     */
    public final float originY;

    /**
     * Change in x coordinate since the start of the drag (in percentage of the screen)
     */
    public final float translX;

    /**
     * Change in y coordinate since the start of the drag (in percentage of the screen)
     */
    public final float translY;

    /**
     * Change in x coordinate since the last drag event (in percentage of the screen)
     */
    public final float dx;

    /**
     * Change in y coordinate since the last drag event (in percentage of the screen)
     */
    public final float dy;

    public EntityDraggedEvent(Entity entity, float originX, float originY, float translX, float translY, float dx, float dy) {
        this.entity = entity;
        this.originX = originX;
        this.originY = originY;
        this.translX = translX;
        this.translY = translY;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public List<Entity> getEntities() {
        return List.of(this.entity);
    }
}
