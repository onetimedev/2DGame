package scc210game.engine.utils;

import java.io.Serializable;

@FunctionalInterface
public interface SerializableBiConsumer<A, B> extends Serializable {
	void accept(A a, B b);
}
