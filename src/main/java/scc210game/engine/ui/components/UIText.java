package scc210game.engine.ui.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

import java.util.Map;

public class UIText extends Component {
    static {
        register(UIText.class, UIText::new);
    }

    public String text;

    public UIText(String text) {
        this.text = text;
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("text", this.text));
    }
}
