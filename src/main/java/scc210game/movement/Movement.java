package scc210game.movement;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
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
    var view = world.fetchGlobalResource(MainViewResource.class);

    if (e instanceof KeyPressedEvent) {
      KeyPressedEvent e1 = (KeyPressedEvent) e;
      var position = world.fetchComponent(playerEnt, Position.class);

      //int moveX = 64;
      //int moveY = 64;

      switch (e1.key) {
        case A: {
          position.xPos -= 1;
          //moveX = -64;
          //moveY = 0;
          break;
        }
        case S: {
          position.yPos += 1;
          //moveX = 0;
          break;
        }
        case D: {
          position.xPos += 1;
          //moveY = 0;
          break;
        }
        case W: {
          position.yPos -= 1;
          //moveY = -64;
          //moveX = 0;
          break;
        }
        default:
          //throw new IllegalStateException("Unexpected value: " + e1.key);
      }

      java.lang.System.out.println("Player Pos: " + position.xPos + "," + position.yPos);
      //view.mainView.setCenter(view.mainView.getCenter().x + moveX, view.mainView.getCenter().y + moveY);

    }
  }


}
