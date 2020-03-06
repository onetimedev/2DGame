package scc210game.engine.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class ResourceLoader {
    private static final HashMap<String, byte[]> cache = new HashMap<>();

    private static final ClassLoader loader = ClassLoader.getSystemClassLoader();

    public static InputStream resolve(String s) {
        byte[] bytes;
        if (cache.containsKey(s))
            bytes = cache.get(s);
        else {
            var stream = Objects.requireNonNull(loader.getResourceAsStream(s));
            try {
                bytes = stream.readAllBytes();
                cache.put(s, bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new ByteArrayInputStream(bytes);
    }
}
