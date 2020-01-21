package scc210game.utils;

@FunctionalInterface
public interface TriConsumer<A, B, C> {
	void accept(A a, B b, C c);

}
