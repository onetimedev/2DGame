package scc210game.utils;

import javax.annotation.Nonnull;

public class Tuple2<L, R> {
    @Nonnull
    public final L l;
    @Nonnull
    public final R r;

    public Tuple2(@Nonnull L l, @Nonnull R r) {
        this.l = l;
        this.r = r;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;

        if (!this.l.equals(tuple2.l)) return false;
        return this.r.equals(tuple2.r);
    }

    @Override
    public int hashCode() {
        int result = this.l.hashCode();
        result = 31 * result + this.r.hashCode();
        return result;
    }
}
