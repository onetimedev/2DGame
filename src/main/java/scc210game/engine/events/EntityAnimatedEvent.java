package scc210game.engine.events;

import scc210game.engine.ecs.Entity;

import java.util.List;

public class EntityAnimatedEvent extends UiEvent implements ContainsEntities {

    @Override
    public List<Entity> getEntities() {
        return null;
    }
}
