package scc210game.engine.combat;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.ui.components.UITransform;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatPlayer;

import java.util.Random;

public class CombatUtils
{
    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int DOWN = 3;
    public static final int ROTATE = 2;
    public static final int PLAYER = 0;
    public static final int BOSS = 1;

    public static final float X_AXIS_MOVE_DISTANCE = 0.003f;
    public static final float Y_AXIS_MOVE_DISTANCE = 0.005f;

    public static String PLAYER_SPRITE = "src/main/resources/textures/Combat/Combat-player-sprite-sheet.png";
    public static String TARGET_TEXTURE = "src/main/resources/textures/Combat/focus.png";
    public static float WEAPON_PADDING = 0.01f;

    public static int MAX_HEALTH = 400;
    public static int STARTING_HEALTH = 400;

    public boolean hasCollided(UITransform rect1, UITransform rect2)
    {

        if (rect1.xPos < rect2.xPos + rect2.width &&
                rect1.xPos + rect1.width > rect2.xPos &&
                rect1.yPos < rect2.yPos + rect2.height &&
                rect1.yPos + rect1.height > rect2.yPos) {
            return true;
        }


        return false;

    }



    public UITransform getOpponent(World w, boolean enemy){
        if(enemy){
            var combatPlayer = w.applyQuery(Query.builder().require(CombatEnemy.class).build()).findFirst().get();
            return w.fetchComponent(combatPlayer, UITransform.class);
        }else {
            var combatPlayer = w.applyQuery(Query.builder().require(CombatPlayer.class).build()).findFirst().get();
            return w.fetchComponent(combatPlayer, UITransform.class);
        }
    }


    public UITransform getWeapon(World w, Class<? extends Component> weaponClass){
        var combatPlayer = w.applyQuery(Query.builder().require(weaponClass).build()).findFirst().get();
        return w.fetchComponent(combatPlayer, UITransform.class);

    }

    public void damagePlayer(World w, int damage)
    {
        var handle = w.applyQuery(Query.builder().require(Scoring.class).build()).findFirst().get();
        var scorer = w.fetchComponent(handle, Scoring.class);
        scorer.damagePlayer(damage);
    }

    public void damageEnemy(World w, int damage)
    {
        var handle = w.applyQuery(Query.builder().require(Scoring.class).build()).findFirst().get();
        var scorer = w.fetchComponent(handle, Scoring.class);
        scorer.damageEnemy(damage);
    }


    public CombatResources getCombatResources(World w)
    {
        var handle = w.applyQuery(Query.builder().require(CombatResources.class).build()).findFirst().get();
        return w.fetchComponent(handle, CombatResources.class);
    }

    public float getHealth(World w, boolean enemy)
    {
        var handle = w.applyQuery(Query.builder().require(Scoring.class).build()).findFirst().get();
        var scoringComp = w.fetchComponent(handle, Scoring.class);
        if (enemy)
        {
            if(scoringComp.getEnemyAbsHealth() <= 0){
                return 0.0f;
            }else {
                return scoringComp.getHealthPercentage(scoringComp.getEnemyAbsHealth());
            }

        }
        else
        {
            if(scoringComp.getPlayerAbsHealth() <= 0){
                return 0.0f;
            }else {
                return scoringComp.getHealthPercentage(scoringComp.getPlayerAbsHealth());
            }
        }

    }

    public int getAbsHealth(World w, boolean enemy)
    {
        var handle = w.applyQuery(Query.builder().require(Scoring.class).build()).findFirst().get();
        if(enemy){
            return w.fetchComponent(handle, Scoring.class).getEnemyAbsHealth();
        }else{
            return w.fetchComponent(handle, Scoring.class).getPlayerAbsHealth();
        }
    }

    public float getOffset()
    {
        Random r = new Random();
        float min = 0.01f;
        float max = 0.05f;
        return (min + r.nextFloat() * (max - min));

    }
}
