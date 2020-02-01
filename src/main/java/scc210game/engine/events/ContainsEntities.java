package scc210game.engine.events;

import scc210game.engine.ecs.Entity;

import java.util.List;

/**
 * Represents classes that have an associated entity
 */
public interface ContainsEntities {
    List<Entity> getEntities();
}
