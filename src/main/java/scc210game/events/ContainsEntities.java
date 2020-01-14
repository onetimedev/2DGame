package scc210game.events;

import scc210game.ecs.Entity;

import java.util.List;

/**
 * Represents classes that have an associated entity
 */
public interface ContainsEntities {
    List<Entity> getEntities();
}
