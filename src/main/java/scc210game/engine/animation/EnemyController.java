package scc210game.engine.animation;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.World;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatEnemyWeapon;
import scc210game.game.components.CombatPlayer;
import scc210game.game.components.CombatPlayerWeapon;
import scc210game.game.map.Enemy;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EnemyController {

    private World w;
    private Class<? extends Component> spriteClass;
    private Class<? extends Component> weaponClass;
    private boolean start = true;
    private boolean fight = true;

    private ScheduledExecutorService scheduledExecutorService;

    private int collisionCount = 0;

    public EnemyController(World w, Class<? extends Component> spriteClass, Class<? extends Component> weaponClass){
        this.w = w;
        this.spriteClass = spriteClass;
        this.weaponClass = weaponClass;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void start()
    {


        if(startFirst()){
            start = true;
            scheduledExecutorService.scheduleAtFixedRate(this::initMove, 2, 2, TimeUnit.SECONDS);

        }
        else
        {
            if(start)
            {
                scheduledExecutorService.scheduleAtFixedRate(this::initMove, 2, 2, TimeUnit.SECONDS);

            }

        }


    }


    private void initMove()
    {

        if(collisionCount >= 3){
            new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.BACKWARD, true).animateXAxis();
        }else {
            if (getMove() != 3) {
                System.out.println("enemy moving");
                new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.FORWARD, true).animateXAxis();
            } else if (getMove() != 4) {
                System.out.println("enemy moving");
                new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.BACKWARD, true).animateXAxis();
            } else {
                System.out.println("no move");
            }
        }
    }

    private int getMove()
    {
        return new Random().nextInt((10 - 1) + 1) + 1;

    }


    private boolean startFirst()
    {
        int number = new Random().nextInt((100 - 1) + 1) + 1;
        if(number >= 80 && number <= 100){
            //20% chance
            return true;
        }

        return false;
    }



    public void setStart(boolean bool)
    {
        this.start = bool;
    }

    public void endFight()
    {
        fight = false;
    }

    public void startFight()
    {
        fight = true;
    }

}
