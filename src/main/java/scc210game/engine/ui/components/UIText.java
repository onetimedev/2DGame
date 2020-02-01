package scc210game.engine.ui.components;

import scc210game.engine.ecs.Component;

public class UIText extends Component {
    static {
        register(UIText.class, UIText::new);
    }

    public String text;

    public UIText(String text) {
        this.text = text;
    }

    @Override
    public String serialize() {
        return this.text;
    }
}
