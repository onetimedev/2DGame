package scc210game.engine.ui;

import scc210game.engine.utils.ResourceLoader;

import java.io.IOException;

public class Font {

    public static final org.jsfml.graphics.Font fantasqueSansMono = new org.jsfml.graphics.Font() {{
        try {
            this.loadFromStream(ResourceLoader.resolve("font/FantasqueSansMono-Regular.ttf"));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }};

    public static final org.jsfml.graphics.Font CaladeaRegular = new org.jsfml.graphics.Font() {{
        try {
            this.loadFromStream(ResourceLoader.resolve("font/Caladea-Regular.ttf"));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }};
}
