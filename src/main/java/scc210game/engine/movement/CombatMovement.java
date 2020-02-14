package scc210game.engine.movement;

import org.jsfml.window.Keyboard;
import scc210game.engine.animation.CombatAnimator;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.ECS;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.state.event.KeyDepressedEvent;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.engine.ui.components.UITransform;
import scc210game.game.components.CombatPlayer;
import scc210game.game.components.CombatPlayerWeapon;
import scc210game.game.map.Map;
import scc210game.game.map.Player;
import scc210game.game.spawners.CombatWeapon;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CombatMovement implements System {
    private final EventQueueReader eventReader;
    private float WEAPON_RAISED = -170f;
    private float WEAPON_HOLSTERED = -50f;
    private boolean raised = false;

    int animCounter = 0;
    private ScheduledExecutorService scheduledExecutorService;


    private World world;
    public CombatMovement(ECS ecs) {
        this.eventReader = ecs.eventQueue.makeReader();
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        ecs.eventQueue.listen(this.eventReader, KeyPressedEvent.class);
        ecs.eventQueue.listen(this.eventReader, KeyDepressedEvent.class);
    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = world.ecs.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();
            this.handleMovement(world, e);
        }
    }


    private void handleMovement(World world, Event event)
    {
        if(world.getCombatStatus()) {
            this.world = world;
            var combatPlayerSprite = world.applyQuery(Query.builder().require(CombatPlayer.class).build()).findFirst().get();
            var combatPlayerWeapon = world.applyQuery(Query.builder().require(CombatPlayerWeapon.class).build()).findFirst().get();
            var cplayerPosition = world.fetchComponent(combatPlayerSprite, UITransform.class);
            var cplayerWeaponPosition = world.fetchComponent(combatPlayerWeapon, UITransform.class);


            if (event instanceof KeyPressedEvent) {
                KeyPressedEvent type = (KeyPressedEvent) event;

                switch (type.key) {
                    case A: {
                        //left move

                        /*
                        cplayerPosition.xPos -= 0.01f;
                        cplayerWeaponPosition.xPos -= 0.01f;
                        */

                        new CombatAnimator(world, CombatPlayer.class, 20, false).animate(0,20);


                        break;
                    }
                    case D: {
                        //right move

                        new CombatAnimator(world, CombatPlayer.class, 20, true).animate(0,20);



                        break;
                    }

                    case SPACE: {
                        //attack
                        if(!raised)
                        {
                            this.raised = true;
                            cplayerWeaponPosition.rotation = WEAPON_RAISED;
                            cplayerWeaponPosition.xPos += 0.1f;
                        }
                        break;
                    }

                }
            }else if(event instanceof KeyDepressedEvent){
                KeyDepressedEvent type = (KeyDepressedEvent) event;

                if(type.key == Keyboard.Key.SPACE){
                    if(raised)
                    {
                        raised = false;
                        cplayerWeaponPosition.rotation = WEAPON_HOLSTERED;
                        cplayerWeaponPosition.xPos -= 0.1f;
                    }

                }
            }
        }
    }

    public void animate(int counter, int max){
        if(counter < max){
            scheduledExecutorService.schedule(this::move, 10, TimeUnit.MILLISECONDS);
        }else{
            animCounter = 0;
        }
    }

    public void move(){

        var combatPlayerSprite = world.applyQuery(Query.builder().require(CombatPlayer.class).build()).findFirst().get();
        var combatPlayerWeapon = world.applyQuery(Query.builder().require(CombatPlayerWeapon.class).build()).findFirst().get();
        var cplayerPosition = world.fetchComponent(combatPlayerSprite, UITransform.class);
        var cplayerWeaponPosition = world.fetchComponent(combatPlayerWeapon, UITransform.class);
        cplayerPosition.xPos += 0.005f;
        cplayerWeaponPosition.xPos += 0.005f;
        animCounter++;
        animate(animCounter, 50);
    }



}
