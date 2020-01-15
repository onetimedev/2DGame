package scc210game.health;

public class HealthSystem {
    private int health = 100;

    public int damageTaken(int damage) {
        health = health - damage;
        return health;
    }
    public boolean isDead(int health) {
        if(health <= 0) {
            return true; 
        } else {
            return false;
        }
    }
}
