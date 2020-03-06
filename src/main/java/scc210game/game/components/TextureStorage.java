package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Component;
import scc210game.engine.utils.ResourceLoader;

import java.io.IOException;
import java.util.Map;

public class TextureStorage extends Component {
    static {
        register(TextureStorage.class, j -> {
            var json = (JsonObject) j;

            var path = (String) json.get("path");

            return new TextureStorage(path);
        });
    }

    private String path;
    private Texture texture;

    public TextureStorage(String p) {
        this.reloadTexture(p);
    }

    public Texture getTexture() {
        return this.texture;
    }

    public String getPath() {
        return this.path;
    }

    public void reloadTexture(String p) {
        this.path = p;
        this.texture = new Texture();
        try {
            this.texture.loadFromStream(ResourceLoader.resolve(this.path));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("path", this.path));
    }

    @Override
    public TextureStorage copy() {
        return new TextureStorage(this.path);
    }
}
