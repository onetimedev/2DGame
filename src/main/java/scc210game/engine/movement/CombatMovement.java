package scc210game.engine.movement;

import org.jsfml.window.Keyboard;
import scc210game.engine.combat.CombatAnimator;
import scc210game.engine.combat.CombatUtils;
import scc210game.engine.combat.Scoring;
import scc210game.engine.ecs.*;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.System;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.state.event.KeyDepressedEvent;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.engine.ui.components.UITransform;
import scc210game.game.components.CombatPlayer;
import scc210game.game.components.CombatPlayerWeapon;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CombatMovement implements System {
    private final EventQueueReader eventReader;
    private float WEAPON_RAISED = -70f;
    private float WEAPON_HOLSTERED = 0f;

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

    @SuppressWarnings("unchecked")
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

                        new CombatAnimator(world, CombatPlayer.class, CombatPlayerWeapon.class, 20, CombatUtils.BACKWARD, false).animateXAxis();


                        break;
                    }
                    case D: {
                        //right move

                        new CombatAnimator(world, CombatPlayer.class, CombatPlayerWeapon.class, 20, CombatUtils.FORWARD, false).animateXAxis();


                        break;
                    }

                    case SPACE: {
                        //attack
                        if(!new CombatUtils().getCombatResources(world).getPlayerWeaponRaised())
                        {
                            new CombatUtils().getCombatResources(world).raisePlayerWeapon();
                            cplayerWeaponPosition.rotation = WEAPON_RAISED;
                            //cplayerWeaponPosition.xPos += 0.1f;
                        }
                        break;
                    }

                }
            }else if(event instanceof KeyDepressedEvent){
                KeyDepressedEvent type = (KeyDepressedEvent) event;

                if(type.key == Keyboard.Key.SPACE){
                    if(new CombatUtils().getCombatResources(world).getPlayerWeaponRaised())
                    {
                        new CombatUtils().getCombatResources(world).lowerPlayerWeapon();
                        cplayerWeaponPosition.rotation = WEAPON_HOLSTERED;
                        //cplayerWeaponPosition.xPos -= 0.1f;
                    }

                }
            }
        }
    }



}
