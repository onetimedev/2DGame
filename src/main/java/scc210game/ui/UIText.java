package scc210game.ui;

import scc210game.ecs.Component;

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
