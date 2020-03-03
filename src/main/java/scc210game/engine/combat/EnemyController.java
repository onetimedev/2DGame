package scc210game.engine.combat;

import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.ui.components.UITransform;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatEnemyWeapon;

import java.io.IOException;
import java.nio.file.Paths;
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

    private int damage;


    public EnemyController(World w, Class<? extends Component> spriteClass, Class<? extends Component> weaponClass, int damage){
        this.w = w;
        this.spriteClass = spriteClass;
        this.weaponClass = weaponClass;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.damage = damage;
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

    private int getHealth()
    {
        return new CombatUtils().getAbsHealth(w, true);

    }

    private void animateSprite()
    {
        //scheduledExecutorService.schedule(this::animate, 200, TimeUnit.MILLISECONDS);
        getSprite().nextChange = System.currentTimeMillis() + 60;
        if(getSprite().state < 3) {
            getSprite().state++;
        }
        getSprite().signal = true;
    }




    private CombatSprite getSprite()
    {
        var spriteState = w.applyQuery(Query.builder().require(CombatSprite.class).build()).findFirst().orElseThrow();
        return w.fetchComponent(spriteState, CombatSprite.class);
    }


    private void initMove()
    {
        if(w.getActiveAnimation())
        {
            if(getHealth() > 0) {

                if (collisionCount >= 3) {
                    new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.BACKWARD, true).animateXAxis();
                    collisionCount = 0;
                    getSprite().state = 0;
                    getSprite().signal = false;
                } else {
                    if (getMove() != 3) {
                        //forward move
                        //System.out.println("enemy moving forward");
                        UITransform attributes = new CombatUtils().getOpponent(w, true);
                        float collisionXPos = attributes.xPos + (CombatUtils.X_AXIS_MOVE_DISTANCE * 15);
                        UITransform newAttr = new UITransform(attributes.xPos, attributes.yPos, attributes.zPos, attributes.width, attributes.height);

                        if (new CombatUtils().hasCollided(newAttr, new CombatUtils().getOpponent(w, false))) {
                            //System.out.println("collided so moving backward");
                            collisionCount++;
                            new CombatUtils().damagePlayer(w, damage);
                            new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.FORWARD, true).animateXAxis();
                            this.animateSprite();
                        } else {
                            //System.out.println("moving forward");
                            new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.FORWARD, true).animateXAxis();
                        }
                    } else if (getMove() != 4) {
                        //backwards move
                        //System.out.println("enemy moving backward");
                        new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.BACKWARD, true).animateXAxis();
                    } else {
                        //System.out.println("no move");
                    }
                }

            }else
            {
                animateDeath();
                scheduledExecutorService.shutdown();
            }


        }else{
            scheduledExecutorService.shutdown();
        }
    }


    public void animateDeath()
    {
        new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 150, CombatUtils.DOWN, true).animateYAxis();
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