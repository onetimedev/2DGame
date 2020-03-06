package scc210game.engine.utils;

import java.io.InputStream;
import java.util.Objects;

public class ResourceLoader {
    private static final ClassLoader loader = ClassLoader.getSystemClassLoader();

    public static InputStream resolve(String s) {
        return Objects.requireNonNull(loader.getResourceAsStream(s));
    }
}
