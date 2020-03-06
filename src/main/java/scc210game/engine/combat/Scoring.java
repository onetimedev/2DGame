package scc210game.engine.combat;

import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.JsonObject;
import scc210game.engine.ecs.Component;
import scc210game.game.map.Enemy;
import scc210game.game.utils.LoadJsonNum;

import java.util.Map;

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
        //System.out.println("damaged enemy");
    }

    public void damagePlayer(int damage)
    {
        this.playerHealth -= damage;
        //System.out.println("damaged player");
    }

    public void addExperience()
    {
        this.playerExperience++;
    }



    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("enemyhealth", this.enemyHealth, "playerhealth", this.playerHealth, "playerexperience", this.playerExperience));
    }


    static {
        register(Scoring.class, j -> {
            var json = (JsonObject) j;
            return new Scoring(LoadJsonNum.loadInt(json.get("enemyhealth")), LoadJsonNum.loadInt(json.get("playerhealth")), LoadJsonNum.loadInt(json.get("playerexperience")));
        });
    }


}
