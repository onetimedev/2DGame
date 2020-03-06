package scc210game.engine.utils;

import javax.annotation.Nonnull;

public class Tuple3<A, B, C> {
    @Nonnull
    public final A a;
    @Nonnull
    public final B b;
    @Nonnull
    public final C c;


    public Tuple3(@Nonnull A a, @Nonnull B b, @Nonnull C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;

        if (!a.equals(tuple3.a)) return false;
        if (!b.equals(tuple3.b)) return false;
        return c.equals(tuple3.c);
    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        result = 31 * result + c.hashCode();
        return result;
    }
}
