package scc210game.engine.combat;

import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class CombatImage extends Component {

    private String path;

    public CombatImage(String path)
    {
        this.path = path;
    }

    public String getPath()
    {
        return this.path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }


    @Override
    public Jsonable serialize() {
        return null;
    }
}
