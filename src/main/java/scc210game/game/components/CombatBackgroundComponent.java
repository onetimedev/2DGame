package scc210game.game.components;

import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class CombatBackgroundComponent extends Component {

    public String path;

    public CombatBackgroundComponent(String name) {
        path = name;
    }

    @Override
    public Jsonable serialize() {
        return null;
    }
}
