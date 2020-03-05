package scc210game.game.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class LoadJsonNum {
    public static int loadInt(Object o) {
        if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof BigDecimal) {
            return ((BigDecimal) o).intValue();
        } else if (o instanceof BigInteger) {
            return ((BigInteger) o).intValue();
        }

        throw new RuntimeException("aaa");
    }

    public static float loadFloat(Object o) {
        if (o instanceof Float) {
            return (Float) o;
        } else if (o instanceof BigDecimal) {
            return ((BigDecimal) o).floatValue();
        }

        throw new RuntimeException("aaa");
    }

    public static long loadLong(Object o) {
        if (o instanceof Long) {
            return (Long) o;
        } else if (o instanceof BigDecimal) {
            return ((BigDecimal) o).longValue();
        } else if (o instanceof BigInteger) {
            return ((BigInteger) o).longValue();
        }

        throw new RuntimeException("aaa");
    }
}
