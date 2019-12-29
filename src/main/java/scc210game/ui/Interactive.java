package scc210game.ui;

import scc210game.ecs.Component;

/**
 * Component that flags entities that can be interacted with using the mouse
 */
public class Interactive extends Component {
    static {
        register(Interactive.class, s -> new Interactive());
    }

    @Override
    public String serialize() {
        return "";
    }
}
