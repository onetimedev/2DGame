package scc210game.game.map;

import scc210game.engine.ecs.Component;

public class Player extends Component {

    static {
        register(Player.class, s -> new Player());
    }

    @Override
    public String serialize() {
        return "";
    }
}
