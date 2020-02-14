package scc210game.engine.animation;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.ui.components.UITransform;
import scc210game.game.components.CombatPlayer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CombatAnimator {

    private ScheduledExecutorService scheduledExecutorService;
    private int animationCounter;
    private int animationMax;
    private boolean forward;
    private World world;
    private Class<? extends Component> objToAnimate;

    public CombatAnimator(World world, Class<? extends Component> component, int max, boolean forward){
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.world = world;
        this.objToAnimate = component;
        animationMax = max;
        animationCounter = 0;
        this.forward = forward;

    }

    public void animate(int counter, int max)
    {
        if(counter < max){
            scheduledExecutorService.schedule(this::moveX, 10, TimeUnit.MILLISECONDS);
        }else{
            animationCounter = 0;
        }
    }

    private void moveX()
    {
        var obj = world.applyQuery(Query.builder().require(objToAnimate).build()).findFirst().get();
        var cplayerPosition = world.fetchComponent(obj, UITransform.class);
        if(forward) {
            cplayerPosition.xPos += 0.005f;
        }else{
            cplayerPosition.xPos -= 0.005f;
        }
        animationCounter++;
        animate(animationCounter, animationMax);


    }
}
