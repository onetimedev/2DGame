package scc210game.engine.Position;

import scc210game.engine.ecs.Component;

public class EnemyPos extends Component {
    public int x;
    public int y;

    public EnemyPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String serialize() {
        return null;
    }
}
