package scc210game.ui;

import scc210game.ecs.Component;

/**
 * Represents UI entities that are currently being displayed
 */
public class UIActive extends Component {
    static {
        register(UIActive.class, s -> new UIActive());
    }

    @Override
    public String serialize() {
        return "";
    }
}
