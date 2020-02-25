package scc210game.engine.combat;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.ui.components.UITransform;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CombatAnimator {

    private ScheduledExecutorService scheduledExecutorService;
    private int animationCounter;
    private int animationMax;
    private int direction;
    private World world;
    private Class<? extends Component> spriteClass;
    private Class<? extends Component> weaponClass;

    boolean enemy;


    public CombatAnimator(World world, Class<? extends Component> spriteClass, Class<? extends Component> weaponClass, int max, int direction, boolean enemy){
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.world = world;
        this.direction = direction;

        this.spriteClass = spriteClass;
        this.weaponClass = weaponClass;

        animationMax = max;
        animationCounter = 0;

        this.enemy = enemy;
    }


    public void animateXAxis()
    {
        if(animationCounter < animationMax)
        {
            if(!enemy) {
                scheduledExecutorService.schedule(this::moveXPlayerSprite, 10, TimeUnit.MILLISECONDS);
            }else{
                scheduledExecutorService.schedule(this::moveXEnemySprite, 10, TimeUnit.MILLISECONDS);
            }
        }else{
           exit();
        }
    }


    private void moveXPlayerSprite()
    {


        var sprite = world.applyQuery(Query.builder().require(spriteClass).build()).findFirst().get();
        var spriteAttributes = world.fetchComponent(sprite, UITransform.class);

        var weapon = world.applyQuery(Query.builder().require(weaponClass).build()).findFirst().get();
        var weaponAttributes = world.fetchComponent(weapon, UITransform.class);

        switch (this.direction){
            case CombatUtils.FORWARD: {
                if(!new CombatUtils().hasCollided(spriteAttributes, new CombatUtils().getOpponent(world, true))) {
                    spriteAttributes.xPos += CombatUtils.X_AXIS_MOVE_DISTANCE;
                    weaponAttributes.xPos += CombatUtils.X_AXIS_MOVE_DISTANCE;
                    continueAnimation();
                }else{
                    if(new CombatUtils().getCombatResources(world).getPlayerWeaponRaised())
                    {
                        new CombatUtils().damageEnemy(world);
                    }
                    exit();
                }
                break;
            }

            case CombatUtils.BACKWARD: {
                if(!(spriteAttributes.xPos <= getEnd())) {
                    spriteAttributes.xPos -= CombatUtils.X_AXIS_MOVE_DISTANCE;
                    weaponAttributes.xPos -= CombatUtils.X_AXIS_MOVE_DISTANCE;

                    continueAnimation();
                }else{
                    exit();
                }
                break;
            }

        }



    }


    private void moveXEnemySprite()
    {


        var sprite = world.applyQuery(Query.builder().require(spriteClass).build()).findFirst().get();
        var spriteAttributes = world.fetchComponent(sprite, UITransform.class);

        switch (this.direction){
            case CombatUtils.FORWARD: {
                if(!new CombatUtils().hasCollided(spriteAttributes, new CombatUtils().getOpponent(world, false))) {
                    spriteAttributes.xPos -= CombatUtils.X_AXIS_MOVE_DISTANCE;

                    continueAnimation();
                }else{
                    System.out.println("enemy exiting");
                    exit();
                }
                break;
            }

            case CombatUtils.BACKWARD: {
                if(spriteAttributes.xPos <= getEnd()) {

                    spriteAttributes.xPos += CombatUtils.X_AXIS_MOVE_DISTANCE;

                    continueAnimation();
                }else{
                    System.out.println("enemy exiting");
                    exit();
                }
                break;
            }

        }



    }

    private void exit()
    {
        animationCounter = 0;
        animationMax = 0;
        scheduledExecutorService.shutdown();
    }

    private void continueAnimation()
    {
        animationCounter++;
        animateXAxis();
    }

    private float getEnd()
    {
        return enemy ? 0.75f : 0.0f;
    }



}
