package scc210game.engine.combat;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.events.ExitCombatState;
import scc210game.engine.events.LeaveCombatEvent;
import scc210game.engine.ui.components.UITransform;
import scc210game.game.components.*;

import java.util.Random;
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
                scheduledExecutorService.schedule(this::moveXPlayerSprite, 15, TimeUnit.MILLISECONDS);
            }else{
                scheduledExecutorService.schedule(this::moveXEnemySprite, 15, TimeUnit.MILLISECONDS);
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
        var damage = world.fetchComponent(weapon, CombatPlayerWeapon.class);

        var targetEntity = world.applyQuery(Query.builder().require(TargetPosition.class).build()).findFirst().get();
        var target =  world.fetchComponent(targetEntity, TargetPosition.class);

        var enemyPos = world.applyQuery(Query.builder().require(CombatEnemy.class).build()).findFirst().get();
        var enemyPosAttr =  world.fetchComponent(enemyPos, UITransform.class);




        UITransform modWeaponAttributes = new UITransform(weaponAttributes);
        modWeaponAttributes.xPos = modWeaponAttributes.xPos + CombatUtils.WEAPON_PADDING;
        switch (this.direction)
        {
            case CombatUtils.FORWARD: {
                if(!new CombatUtils().hasCollided(spriteAttributes, new CombatUtils().getOpponent(world, true)))
                {
                    spriteAttributes.xPos += CombatUtils.X_AXIS_MOVE_DISTANCE;
                    //weaponAttributes.xPos += CombatUtils.X_AXIS_MOVE_DISTANCE;weaponAttributes.xPos = (spriteAttributes.xPos)+0.0144f;
                    weaponAttributes.xPos = (spriteAttributes.xPos) + 0.19f;
                    continueXAxisAnimation();
                }
                else if(new CombatUtils().hasCollided(spriteAttributes, new CombatUtils().getOpponent(world, true)) || new CombatUtils().hasCollided(modWeaponAttributes, new CombatUtils().getOpponent(world, true)))
                {
                    if(new CombatUtils().hasCollided(modWeaponAttributes, new CombatUtils().getOpponent(world, true))) {
                        if (new CombatUtils().getCombatResources(world).getPlayerWeaponRaised()) {
                            new CombatUtils().damageEnemy(world, damage.damage);
                            if(!target.visible) {
                                target.xPos = weaponAttributes.xPos;
                                target.yPos = weaponAttributes.yPos;
                                target.visible = true;
                                target.visibleUntil = System.currentTimeMillis() + TargetPosition.TIMEOUT;
                                target.offset = new CombatUtils().getOffset();
                            }
                        }
                    }
                    exit();
                }
                break;
            }

            case CombatUtils.BACKWARD: {
                if(!(spriteAttributes.xPos <= getEnd()))
                {
                    spriteAttributes.xPos -= CombatUtils.X_AXIS_MOVE_DISTANCE;
                    weaponAttributes.xPos = (spriteAttributes.xPos) + 0.19f;

                    continueXAxisAnimation();
                }
                else
                {
                    exit();
                }
                break;
            }

        }



    }


    public void animateYAxis()
    {
        if(animationCounter < animationMax) {
            if (!enemy) {

            } else {
                scheduledExecutorService.schedule(this::moveYEnemySprite, 15, TimeUnit.MILLISECONDS);
            }
        }else{
            exitCombat();

        }
    }

    private void moveYEnemySprite()
    {
        switch(this.direction){
            case CombatUtils.DOWN:{
                var sprite = world.applyQuery(Query.builder().require(spriteClass).build()).findFirst().get();
                var spriteAttributes = world.fetchComponent(sprite, UITransform.class);
                spriteAttributes.yPos += CombatUtils.Y_AXIS_MOVE_DISTANCE;

                if(weaponClass != null){
                    var spriteWeapon = world.applyQuery(Query.builder().require(weaponClass).build()).findFirst().get();
                    var spriteWeaponAttributes = world.fetchComponent(spriteWeapon, UITransform.class);
                    spriteWeaponAttributes.yPos += CombatUtils.Y_AXIS_MOVE_DISTANCE;
                }

                continueYAxisAnimation();
            }
        }
    }

    private void moveXEnemySprite()
    {


        var sprite = world.applyQuery(Query.builder().require(spriteClass).build()).findFirst().get();
        var spriteAttributes = world.fetchComponent(sprite, UITransform.class);

        switch (this.direction)
        {
            case CombatUtils.FORWARD: {
                if(!new CombatUtils().hasCollided(spriteAttributes, new CombatUtils().getOpponent(world, false)))
                {
                    spriteAttributes.xPos -= CombatUtils.X_AXIS_MOVE_DISTANCE;

                    continueXAxisAnimation();
                }
                else
                {
                    System.out.println("enemy exiting");
                    exit();
                }
                break;
            }

            case CombatUtils.BACKWARD: {
                if(spriteAttributes.xPos <= getEnd())
                {

                    spriteAttributes.xPos += CombatUtils.X_AXIS_MOVE_DISTANCE;

                    continueXAxisAnimation();
                }
                else
                {
                    System.out.println("enemy exiting");
                    exit();
                }
                break;
            }

        }



    }

    private void exit()
    {
        var cLock = world.applyQuery(Query.builder().require(ControlLock.class).build()).findFirst().orElseThrow();
        var lock = world.fetchComponent(cLock, ControlLock.class);
        if(lock.isLocked()) lock.unlock();
        animationCounter = 0;
        animationMax = 0;
        scheduledExecutorService.shutdown();

    }


    private void exitCombat()
    {
        animationCounter = 0;
        animationMax = 0;
        scheduledExecutorService.shutdown();

        var combatInfo = world.applyQuery(Query.builder().require(CombatInfo.class).build()).findFirst().orElseThrow();
        var info = world.fetchComponent(combatInfo, CombatInfo.class);
        info.didPlayerWin = spriteClass != CombatPlayer.class;
        world.deactivateCombat();
        world.ecs.acceptEvent(new ExitCombatState());

    }
    private void continueXAxisAnimation()
    {
        animationCounter++;
        animateXAxis();
    }

    private void continueYAxisAnimation()
    {
        animationCounter++;
        animateYAxis();
    }




    private float getEnd()
    {
        return enemy ? 0.65f : 0.0f;
    }


}
