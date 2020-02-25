package scc210game.engine.ecs;

/**
 * Abstract class representing Components of entities
 */
public abstract class Component extends SerDe implements Copyable<Component> {
    @Override
    public Component copy() {
        throw new RuntimeException("Copyable::copy required but not implemented for: " + this.getClass().getName());
    }
}
