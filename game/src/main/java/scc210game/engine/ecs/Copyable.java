package scc210game.engine.ecs;

public interface Copyable<T extends Copyable<T>> {
    default T copy() {
        throw new RuntimeException("Class: " + this.getClass().getName() + " does not support copy");
    }
}
