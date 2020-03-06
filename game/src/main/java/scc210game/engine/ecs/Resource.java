package scc210game.engine.ecs;

/**
 * Abstract class representing resources,
 * which are components that are not directly related to an entity
 */
public abstract class Resource extends SerDe {
    /**
     * Set to false to flag a component as non-persisting between reloads
     */
    public boolean shouldKeep() {
        return true;
    }
}
