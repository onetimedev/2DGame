package scc210game.health;

import scc210game.ecs.Component;

public class HealthSystem extends Component {
    private int health;

    /**
     * get initial health
     */
    public HealthSystem(int health) {
        this.health = health;
    }

    /**
     * getter and setter functions for health
     * setter function will set new health when taken damage
     */
    public void setHealth(int health) {
        this.health = health;
    }
    public int getHealth() {
        return this.health;
    }

    /**
     * check if health is 0
     */
    public boolean isDead() {
        if(this.health <= 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String serialize() {
        return null;
    }
}
