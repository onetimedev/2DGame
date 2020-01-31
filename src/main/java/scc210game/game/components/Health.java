package scc210game.game.components;

import scc210game.engine.ecs.Component;

public class Health extends Component {
    public int health;

    /**
     * get initial health
     */
    public Health(int health) {
        this.health = health;
    }

    /**
     * check if health is 0
     */
    public boolean isDead() {
        return this.health <= 0;
    }

    @Override
    public String serialize() {
        return null;
    }
}
