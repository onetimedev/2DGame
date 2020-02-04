package scc210game.engine.movement;

import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.ecs.Query;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueue;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.game.map.Map;
import scc210game.game.map.Player;


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
    var position = world.fetchComponent(playerEnt, Position.class);
    var mapEnt = world.applyQuery(Query.builder().require(Map.class).build()).findFirst().get();
    var map = world.fetchComponent(mapEnt, Map.class);


    if (e instanceof KeyPressedEvent) {
      KeyPressedEvent e1 = (KeyPressedEvent) e;

      switch (e1.key) {
        case A: {
          if(!map.getTile(position.xPos-1, position.yPos).hasCollision())
            position.xPos -= 1;
          break;
        }
        case S: {
          if(!map.getTile(position.xPos, position.yPos+1).hasCollision())
            position.yPos += 1;
          break;
        }
        case D: {
          if(!map.getTile(position.xPos+1, position.yPos).hasCollision())
            position.xPos += 1;
          break;
        }
        case W: {
          if(!map.getTile(position.xPos, position.yPos-1).hasCollision())
            position.yPos -= 1;
          break;
        }
      }
      //java.lang.System.out.println("Player Pos: " + position.xPos + "," + position.yPos);
    }
    var view = world.fetchGlobalResource(MainViewResource.class);
    view.mainView.setCenter(position.xPos*64, position.yPos*64);
  }


}