package scc210game.engine.combat;

import scc210game.engine.ecs.Component;

public class Scoring extends Component {


    private int playerExperiance;
    private int playerHealth;

    private int enemyHealth;


    public Scoring(int playerExperiance, int playerHealth, int enemyHealth)
    {

        this.playerExperiance = playerExperiance;
        this.playerHealth = playerHealth;
        this.enemyHealth = enemyHealth;
    }



    public int getPlayerExperiance()
    {
        return this.playerExperiance;
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



    @Override
    public String serialize() {
        return null;
    }
}
