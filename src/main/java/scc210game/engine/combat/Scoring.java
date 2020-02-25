package scc210game.engine.combat;

import scc210game.engine.ecs.Component;

public class Scoring extends Component {


    private int playerExperience;
    private int playerHealth;
    private int enemyHealth;


    public Scoring(int playerExperience, int playerHealth, int enemyHealth)
    {

        this.playerExperience = playerExperience;
        this.playerHealth = playerHealth;
        this.enemyHealth = enemyHealth;
    }



    public int getPlayerExperience()
    {
        return this.playerExperience;
    }

    public int getPlayerHealth()
    {
       return this.playerHealth;
    }

    public int getEnemyHealth()
    {
        return this.enemyHealth;
    }


    public void damageEnemy()
    {
        this.enemyHealth--;
        System.out.println("damaged enemy");
    }

    public void damagePlayer()
    {
        this.playerHealth--;
        System.out.println("damaged player");
    }

    public void addExperience()
    {
        this.playerExperience++;
    }



    @Override
    public String serialize() {
        return null;
    }
}
