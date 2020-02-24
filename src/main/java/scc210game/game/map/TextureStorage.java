package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Component;
import scc210game.engine.utils.ResourceLoader;

import java.io.IOException;
import java.util.Map;

public class TextureStorage extends Component {
    private String path;
    public Texture texture;

    public TextureStorage(String p) {
        this.reloadTexture(p);
    }

    public void reloadTexture(String p) {
        this.path = p;
        this.texture = new Texture();
        try {
            this.texture.loadFromFile(ResourceLoader.resolve(this.path));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("path", this.path));
    }
}
