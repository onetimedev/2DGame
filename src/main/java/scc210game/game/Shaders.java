package scc210game.game;

import org.jsfml.graphics.Shader;
import org.jsfml.graphics.ShaderSourceException;
import scc210game.engine.utils.ResourceLoader;

import java.io.IOException;

public class Shaders {
    public static final Shader lighter = new Shader() {{
        try {
            this.loadFromFile(ResourceLoader.resolve("shaders/lighter.frag"), Type.FRAGMENT);
        } catch (final IOException | ShaderSourceException e) {
            throw new RuntimeException(e);
        }
    }};

    public static final Shader water = new Shader() {{
        try {
            this.loadFromFile(ResourceLoader.resolve("shaders/water.frag"), Type.FRAGMENT);
        } catch (final IOException | ShaderSourceException e) {
            throw new RuntimeException(e);
        }

        this.setParameter("rot", 0.0f);
    }};
}
