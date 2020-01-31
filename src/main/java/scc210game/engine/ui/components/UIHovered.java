package scc210game.engine.ui.components;

import scc210game.engine.ecs.Component;

/**
 * Component that flags entities that are currently being hovered
 */
public class UIHovered extends Component {
    static {
        register(UIHovered.class, s -> new UIHovered());
    }

    @Override
    public String serialize() {
        return "";
    }
}
