package scc210game.engine.utils;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ResourceLoader {
    private static final ClassLoader loader = ClassLoader.getSystemClassLoader();

    public static Path resolve(String s) {
        try {
            return Paths.get(Objects.requireNonNull(loader.getResource(s)).toURI());
        } catch (final URISyntaxException | NullPointerException e) {
            System.err.println("Couldn't load: " + s);
            throw new RuntimeException(e);
        }
    }
}
