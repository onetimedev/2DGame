package scc210game.engine.combat;

import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class Scoring extends Component {


    public int playerExperience;
    public int playerHealth;
    public int enemyHealth;

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


    public int getPlayerAbsHealth()

    {
        return this.playerHealth;
    }
    public int getEnemyAbsHealth()
    {
        return this.enemyHealth;
    }

    public float getHealthPercentage(int healthVal)
    {
        return ((float)healthVal/(float) new CombatUtils().MAX_HEALTH);
    }



    public void damageEnemy(int damage)
    {
        this.enemyHealth -= damage;
        System.out.println("damaged enemy");
    }

    public void damagePlayer(int damage)
    {
        this.playerHealth -= damage;
        System.out.println("damaged player");
    }

    public void addExperience()
    {
        this.playerExperience++;
    }



    @Override
    public Jsonable serialize() {
        return null;
    }
}