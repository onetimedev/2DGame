package scc210game.engine.utils;

import java.io.Serializable;

@FunctionalInterface
public interface SerializableTriConsumer<A, B, C> extends Serializable {
	void accept(A a, B b, C c);
}
