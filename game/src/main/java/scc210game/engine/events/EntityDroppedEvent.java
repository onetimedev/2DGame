package scc210game.engine.events;

import scc210game.engine.ecs.Entity;

import java.util.List;

/**
 * Fired when an entity was dragged and then dropped onto another
 */
public class EntityDroppedEvent extends UiEvent implements ContainsEntities {
    public final Entity dropped;
    public final Entity droppedOnto;

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

    public EntityDroppedEvent(Entity dropped, Entity droppedOnto, float originX, float originY, float translX, float translY) {
        this.dropped = dropped;
        this.droppedOnto = droppedOnto;
        this.originX = originX;
        this.originY = originY;
        this.translX = translX;
        this.translY = translY;
    }

    @Override
    public List<Entity> getEntities() {
        return List.of(this.dropped, this.droppedOnto);
    }
}
