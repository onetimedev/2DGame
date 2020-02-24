package scc210game.game.map;

import com.github.cliftonlabs.json_simple.Jsonable;
import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Component;

public class TextureStorage extends Component {

    public Texture texture;

    public TextureStorage(Texture t) {
        this.texture = t;
    }


    @Override
    public Jsonable serialize() {
        return null;
    }

}
