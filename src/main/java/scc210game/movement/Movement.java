package scc210game.movement;

import scc210game.ecs.World;
import scc210game.ecs.Query;
import scc210game.events.Event;
import scc210game.state.event.KeyPressedEvent;

import javax.annotation.Nonnull;

public class Movement {
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {

    }
    private void handleEvent(@Nonnull World world, Event e) {
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
                    throw new IllegalStateException("Unexpected value: " + e1.key);
            }

            var playerEnt = world.applyQuery(Query.builder().require(Player.class).build()).findFirst().get();
            var position = world.fetchComponent(playerEnt, Position.class);
            position.x += hMove;
            position.y + -vMove;
        }
    }
}
