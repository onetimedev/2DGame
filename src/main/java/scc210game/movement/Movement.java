package scc210game.movement;

import scc210game.ecs.System;
import scc210game.ecs.World;
import scc210game.ecs.Query;
import scc210game.events.Event;
import scc210game.events.EventQueue;
import scc210game.events.EventQueueReader;
import scc210game.render.MainViewResource;
import scc210game.state.event.KeyPressedEvent;
import scc210game.map.Player;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

public class Movement implements System {
    private final EventQueueReader eventReader;

    public Movement() {
        this.eventReader = EventQueue.makeReader();
        EventQueue.listen(this.eventReader, KeyPressedEvent.class);
    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = EventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();
            this.handleEvent(world, e);
        }
    }

    private void handleEvent(@Nonnull World world, Event e) {
        var playerEnt = world.applyQuery(Query.builder().require(Player.class).build()).findFirst().get();
        //java.lang.System.out.println(view);

        if (e instanceof KeyPressedEvent) {
            KeyPressedEvent e1 = (KeyPressedEvent) e;

            int hMove = 0;
            int vMove = 0;

            switch (e1.key) {
                case A: {
                    hMove -= 1;
                    break;
                }
                case S: {
                    vMove += 1;
                    break;
                }
                case D: {
                    hMove += 1;
                    break;
                }
                case W: {
                    vMove -= 1;
                    break;
                }
                default:
                    //throw new IllegalStateException("Unexpected value: " + e1.key);
            }

            var position = world.fetchComponent(playerEnt, Position.class);
            position.xPos += hMove;
            position.yPos += vMove;

        }
    }


}
