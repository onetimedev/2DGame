package scc210game.game.map;

import scc210game.engine.ecs.Component;

public class FinalBoss extends Component {

    static {
        register(FinalBoss.class, s -> new FinalBoss());
    }

    @Override
    public String serialize() {
        return "";
    }
}
