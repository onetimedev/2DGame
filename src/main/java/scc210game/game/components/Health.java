package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

import java.util.Map;

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
    public Jsonable serialize() {
        return new JsonObject(Map.of(
                "maxHealth", this.maxHealth,
                "health", this.health));
    }
}
