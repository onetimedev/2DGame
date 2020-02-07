package scc210game.game.components;

import scc210game.engine.ecs.Component;

public class Health extends Component {
    public final int maxHealth;
    public int health;

    /**
     * get initial health
     */
    public Health(int maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public float hpPercent() {
        return (float)this.health / (float)this.maxHealth;
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
