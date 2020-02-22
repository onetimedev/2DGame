package scc210game.engine.animation;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.World;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
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

    private float WEAPON_RAISED = 10f;
    private float WEAPON_HOLSTERED = 0f;

    private int collisionCount = 0;

    private boolean weaponRaised = false;

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
            scheduledExecutorService.scheduleAtFixedRate(this::initMove, 0, 800, TimeUnit.MILLISECONDS);

        }
        else
        {
            if(start)
            {
                scheduledExecutorService.scheduleAtFixedRate(this::initMove, 0, 800, TimeUnit.MILLISECONDS);

            }

        }


    }


    private void initMove()
    {
        if(w.getActiveAnimation()) {
            if (collisionCount >= 1) {
                new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.BACKWARD, true).animateXAxis();
                collisionCount = 0;
            } else {
                if (getMove() != 3) {
                    //forward move
                    System.out.println("enemy moving forward");
                    UITransform attributes = new CombatUtils().getOpponent(w, true);
                    float collisionXPos = attributes.xPos + (CombatUtils.X_AXIS_MOVE_DISTANCE * 15);
                    UITransform newAttr = new UITransform(attributes.xPos, attributes.yPos, attributes.zPos, attributes.width, attributes.height);
                    if (new CombatUtils().hasCollided(newAttr, new CombatUtils().getOpponent(w, false))) {
                        if (!weaponRaised) {
                            raiseWeapon();
                            weaponRaised = true;
                        }


                        System.out.println("collided so moving backward");
                        collisionCount++;
                        new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.FORWARD, true).animateXAxis();
                    } else {

                        if (weaponRaised) {
                            lowerWeapon();
                            weaponRaised = false;
                        }
                        System.out.println("moving forward");
                        new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.FORWARD, true).animateXAxis();
                    }
                } else if (getMove() != 4) {
                    //backwards move
                    System.out.println("enemy moving backward");
                    new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.BACKWARD, true).animateXAxis();
                } else {
                    System.out.println("no move");
                }
            }
        }else{
            scheduledExecutorService.shutdown();
        }
    }


    private void raiseWeapon()
    {

        UITransform weapon = new CombatUtils().getWeapon(w, CombatEnemyWeapon.class);
        weapon.rotation = WEAPON_RAISED;
        //weapon.yPos -= 0.15f;


    }


    private void lowerWeapon()
    {
        UITransform weapon =  new CombatUtils().getWeapon(w, CombatEnemyWeapon.class);
        weapon.rotation = WEAPON_HOLSTERED;


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
