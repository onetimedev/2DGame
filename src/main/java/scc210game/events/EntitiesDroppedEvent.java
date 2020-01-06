package scc210game.events;

import scc210game.ecs.Entity;

import java.util.List;

/**
 * Fired when an entity was dragged and then dropped onto another
 */
public class EntitiesDroppedEvent implements UiEvent, ContainsEntities {
    public final Entity dropped;
    public final Entity droppedOnto;

    public EntitiesDroppedEvent(Entity dropped, Entity droppedOnto) {
        this.dropped = dropped;
        this.droppedOnto = droppedOnto;
    }

    @Override
    public List<Entity> getEntities() {
        return List.of(this.dropped, this.droppedOnto);
    }
}
