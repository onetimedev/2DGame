package scc210game.engine.animation;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.ui.components.UITransform;
import scc210game.game.components.CombatPlayer;
import scc210game.game.components.CombatPlayerWeapon;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CombatAnimator {

    private ScheduledExecutorService scheduledExecutorService;
    private int animationCounter;
    private int animationMax;
    private int direction;
    private World world;
    private Class<? extends Component> object;

    public CombatAnimator(World world, Class<? extends Component> component, int max, int direction){
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.world = world;
        this.object = component;
        this.direction = direction;

        animationMax = max;
        animationCounter = 0;


    }


    public void animateXAxis()
    {
        if(animationCounter < animationMax)
        {
            if(object == CombatPlayer.class) {
                scheduledExecutorService.schedule(this::moveX, 10, TimeUnit.MILLISECONDS);
            }else if(object == CombatPlayerWeapon.class){

            }
        }else{
           exit();
        }
    }

    private void moveX()
    {


        var obj = world.applyQuery(Query.builder().require(object).build()).findFirst().get();
        var attributes = world.fetchComponent(obj, UITransform.class);
        switch (this.direction){
            case CombatUtils.FORWARD: {
                if(!new CombatUtils().hasCollided(attributes, new CombatUtils().getEnemy(world))) {
                    attributes.xPos += CombatUtils.X_AXIS_MOVE_DISTANCE;
                    continueAnimation();
                }else{
                    exit();
                }
                break;
            }

            case CombatUtils.BACKWARD: {
                if(!(attributes.xPos <= 0.0)) {
                    attributes.xPos -= CombatUtils.X_AXIS_MOVE_DISTANCE;
                    continueAnimation();
                }else{
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




}
