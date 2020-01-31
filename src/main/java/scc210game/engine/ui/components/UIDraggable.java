package scc210game.engine.ui.components;

import scc210game.engine.ecs.Component;

/**
 * Component that flags entities that can be dragged using the mouse
 */
public class UIDraggable extends Component {
    static {
        register(UIDraggable.class, s -> new UIInteractive());
    }

    @Override
    public String serialize() {
        return "";
    }
}
