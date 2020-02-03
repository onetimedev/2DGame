package scc210game.events;

import scc210game.ecs.Entity;

import java.util.List;

/**
 * Fired when an entity was dragged but the mouse was not released on another entity
 */
public class EntityFailedDroppedEvent extends UiEvent implements ContainsEntities {
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
     * Change in x coordinate at the end of the drag (in percentage of the screen)
     */
    public final float translX;

    /**
     * Change in y coordinate at the end of the drag (in percentage of the screen)
     */
    public final float translY;

    public EntityFailedDroppedEvent(Entity entity, float originX, float originY, float translX, float translY) {
        this.entity = entity;
        this.originX = originX;
        this.originY = originY;
        this.translX = translX;
        this.translY = translY;
    }

    @Override
    public List<Entity> getEntities() {
        return List.of(this.entity);
    }
}
