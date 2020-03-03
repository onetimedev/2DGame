package scc210game.engine.movement;

import org.jsfml.window.Keyboard;
import scc210game.engine.combat.*;
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
    private float WEAPON_RAISED = 180f;
    private float WEAPON_HOLSTERED = 270f;
    boolean left = false;
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

            var spriteState = world.applyQuery(Query.builder().require(CombatSprite.class).build()).findFirst().orElseThrow();
            var state = world.fetchComponent(spriteState, CombatSprite.class);

            if (event instanceof KeyPressedEvent) {
                KeyPressedEvent type = (KeyPressedEvent) event;

                switch (type.key) {
                    case A: {

                        new CombatAnimator(world, CombatPlayer.class, CombatPlayerWeapon.class, 20, CombatUtils.BACKWARD, false).animateXAxis();
                        state.playerSprite = 0;
                        left = true;
                        new CombatUtils().getCombatResources(world).lowerPlayerWeapon();
                        cplayerWeaponPosition.rotation = WEAPON_HOLSTERED;

                        break;
                    }
                    case D: {
                        //right move
                        new CombatAnimator(world, CombatPlayer.class, CombatPlayerWeapon.class, 20, CombatUtils.FORWARD, false).animateXAxis();
                        state.playerSprite = 2;
                        left = false;

                        break;
                    }

                    case SPACE: {
                        //attack
                        if(!new CombatUtils().getCombatResources(world).getPlayerWeaponRaised())
                        {
                            if(!left) {
                                new CombatUtils().getCombatResources(world).raisePlayerWeapon();
                                cplayerWeaponPosition.rotation = WEAPON_RAISED;

                                var sprite = world.applyQuery(Query.builder().require(CombatPlayer.class).build()).findFirst().get();
                                var spriteAttributes = world.fetchComponent(sprite, UITransform.class);

                                var weapon = world.applyQuery(Query.builder().require(CombatPlayerWeapon.class).build()).findFirst().get();
                                var weaponAttributes = world.fetchComponent(weapon, UITransform.class);
                                var damage = world.fetchComponent(weapon, CombatPlayerWeapon.class);
                                UITransform modWeaponAttributes = new UITransform(weaponAttributes);
                                modWeaponAttributes.xPos = modWeaponAttributes.xPos + CombatUtils.WEAPON_PADDING;

                                if(new CombatUtils().hasCollided(modWeaponAttributes, new CombatUtils().getOpponent(world, true)))
                                {
                                    new CombatUtils().damageEnemy(world, damage.damage);

                                }

                                }
                            }
                            //cplayerWeaponPosition.xPos += 0.1f;
                        }
                        break;
                    }

            }else if(event instanceof KeyDepressedEvent){
                KeyDepressedEvent type = (KeyDepressedEvent) event;

                if(type.key == Keyboard.Key.SPACE)
                {
                    if(new CombatUtils().getCombatResources(world).getPlayerWeaponRaised())
                    {

                        new CombatUtils().getCombatResources(world).lowerPlayerWeapon();
                        cplayerWeaponPosition.rotation = WEAPON_HOLSTERED;

                        //cplayerWeaponPosition.xPos -= 0.1f;
                    }

                }
                else if (type.key == Keyboard.Key.D)
                {
                    state.playerSprite = 1;
                }
                else if(type.key == Keyboard.Key.A)
                {
                    state.playerSprite = 1;
                    left = false;
                }
            }

        }
    }



}
