package scc210game.engine.ecs;

/**
 * Abstract class representing Components of entities
 */
public abstract class Component extends SerDe implements Copyable<Component> {
    /**
     * Set to false to flag a component as non-persisting between reloads
     */
    public boolean shouldKeep() {
        return true;
    }
}
