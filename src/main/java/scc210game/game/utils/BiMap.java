package scc210game.game.utils;

import scc210game.engine.utils.Tuple2;

import java.util.HashMap;
import java.util.stream.Stream;

/**
 * A simple bijective map
 *
 * @param <L> the Lhs key/value
 * @param <R> the Rhs key/value
 */
public class BiMap<L, R> {
    private final HashMap<L, R> forward;
    private final HashMap<R, L> backward;

    public BiMap() {
        this.forward = new HashMap<>();
        this.backward = new HashMap<>();
    }

    public void put(L l, R r) {
        this.forward.remove(l);
        this.backward.remove(r);
        this.forward.put(l, r);
        this.backward.put(r, l);
    }

    public boolean containsLeft(L l) {
        return this.forward.containsKey(l);
    }

    public boolean containsRight(R r) {
        return this.backward.containsKey(r);
    }

    public R getByLeft(L l) {
        return this.forward.get(l);
    }

    public L getByRight(R r) {
        return this.backward.get(r);
    }

    public void removeByLeft(L l) {
        var val = this.forward.remove(l);
        this.backward.remove(val);
    }

    public void removeByRight(R r) {
        var val = this.backward.remove(r);
        this.forward.remove(val);
    }

    public Stream<Tuple2<L, R>> items() {
        return this.forward.keySet()
                .stream()
                .map(l -> new Tuple2<>(l, this.forward.get(l)));
    }

    @Override
    public String toString() {
        return "BiMap{" + forward + '}';
    }
}
