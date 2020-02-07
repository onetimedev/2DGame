package scc210game.engine.ecs;

/**
 * Abstract class representing Components of entities
 */
public abstract class Component extends SerDe {
    @Override
    public Component clone() throws CloneNotSupportedException {
        return (Component) super.clone();
    }
}
