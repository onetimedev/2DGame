package scc210game.game.components;

import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class CombatDialog extends Component {

    public String path;
    public boolean active;
    public long activeUntil;


    @Override
    public Jsonable serialize() {
        return null;
    }
}
