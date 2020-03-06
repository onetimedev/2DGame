package scc210game.engine.utils;

import java.io.InputStream;
import java.util.Objects;

public class ResourceLoader {
    private static final ClassLoader loader = ClassLoader.getSystemClassLoader();

    public static InputStream resolve(String s) {
        System.out.println("Resoure Loader Trying to Load: " + s);
        System.out.println(loader.getResourceAsStream(s));
        return Objects.requireNonNull(loader.getResourceAsStream(s));
    }
}
