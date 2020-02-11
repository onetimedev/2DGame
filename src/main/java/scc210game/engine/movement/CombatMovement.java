package scc210game.engine.movement;

import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.ECS;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.engine.ui.components.UITransform;
import scc210game.game.components.CombatPlayer;
import scc210game.game.map.Map;
import scc210game.game.map.Player;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

public class CombatMovement implements System {
    private final EventQueueReader eventReader;

    public CombatMovement(ECS ecs) {
        this.eventReader = ecs.eventQueue.makeReader();
        ecs.eventQueue.listen(this.eventReader, KeyPressedEvent.class);
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
            var combatPlayerSprite = world.applyQuery(Query.builder().require(CombatPlayer.class).build()).findFirst().get();
            var cplayerPosition = world.fetchComponent(combatPlayerSprite, UITransform.class);

            if (event instanceof KeyPressedEvent) {
                KeyPressedEvent type = (KeyPressedEvent) event;

                switch (type.key) {
                    case A: {
                        //left move
                        cplayerPosition.xPos -= 0.01f;
                        break;
                    }
                    case D: {
                        //right move
                        cplayerPosition.xPos += 0.01f;
                        break;
                    }

                    case F: {
                        //attack
                        java.lang.System.out.println("Attack!");
                        break;
                    }

                }
            }
        }
    }



}
