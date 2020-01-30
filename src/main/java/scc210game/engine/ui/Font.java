package scc210game.engine.ui;

import scc210game.engine.utils.ResourceLoader;

import java.io.IOException;

public class Font {
    public static final org.jsfml.graphics.Font freesans = new org.jsfml.graphics.Font() {{
        try {
            this.loadFromFile(ResourceLoader.resolve("font/FreeSans.ttf"));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }};
}
