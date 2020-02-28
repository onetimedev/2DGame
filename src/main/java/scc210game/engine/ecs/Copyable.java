package scc210game.engine.ecs;

public interface Copyable<T extends Copyable<T>> {
    T copy();
}
