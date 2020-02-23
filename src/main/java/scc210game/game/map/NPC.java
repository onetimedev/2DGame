package scc210game.game.map;

import scc210game.engine.ecs.Component;

public class NPC extends Component {

    static {
        register(NPC.class, s -> new NPC());
    }

    @Override
    public String serialize() {
        return "";
    }
}
