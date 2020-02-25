package scc210game.game.components;

import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Component;

public class TextureStorage extends Component {
    public final Texture texture;

    public TextureStorage(Texture texture) {
        this.texture = texture;
    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public Component copy() {
        return this;
    }
}
