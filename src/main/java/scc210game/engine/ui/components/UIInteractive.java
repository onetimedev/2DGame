package scc210game.engine.ui.components;

import scc210game.engine.ecs.Component;

/**
 * Component that flags entities that can be interacted with using the mouse
 */
public class UIInteractive extends Component {
    static {
        register(UIInteractive.class, s -> new UIInteractive());
    }

    @Override
    public String serialize() {
        return "";
    }
}
