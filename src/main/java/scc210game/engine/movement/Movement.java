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
import scc210game.game.map.Map;
import scc210game.game.map.Player;
import scc210game.engine.audio.Audio;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

public class Movement implements System {
  private final EventQueueReader eventReader;

  public Movement(ECS ecs) {
    this.eventReader = ecs.eventQueue.makeReader();
    ecs.eventQueue.listen(this.eventReader, KeyPressedEvent.class);
  }

  @Override
  public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
    for (Iterator<Event> it = world.ecs.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
      Event e = it.next();
      Audio au = new Audio();
      this.handleEvent(world, e, au);
    }
  }

  private void handleEvent(@Nonnull World world, Event e, Audio au) {
    var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
    if (!playerEntO.isPresent())
      return;
    var playerEnt = playerEntO.get();

    var position = world.fetchComponent(playerEnt, Position.class);

    var mapEntO = world.applyQuery(Query.builder().require(Map.class).build()).findFirst();
    if (!mapEntO.isPresent())
      return;
    var mapEnt = mapEntO.get();

    var map = world.fetchComponent(mapEnt, Map.class);


    if (e instanceof KeyPressedEvent) {
      KeyPressedEvent e1 = (KeyPressedEvent) e;

      switch (e1.key) {
        case A: {
          if(!map.getTile(position.xPos-1, position.yPos).hasCollision()) {
            position.xPos -= 1;
            au.walkingSound("walking.mp3");
            //Audio au = walkingSound("./src/main/resources/sounds/walking.mp3");
            //play walking audio
          }
          break;
        }
        case S: {
          if(!map.getTile(position.xPos, position.yPos+1).hasCollision()) {
            position.yPos += 1;
            au.walkingSound("./walking.mp3");
          }
          break;
        }
        case D: {
          if(!map.getTile(position.xPos+1, position.yPos).hasCollision()) {
            position.xPos += 1;
            au.walkingSound("./walking.mp3");
          }
          break;
        }
        case W: {
          if(!map.getTile(position.xPos, position.yPos-1).hasCollision()) {
            position.yPos -= 1;
            au.walkingSound("./walking.mp3");
          }
          break;
        }
      }
      //java.lang.System.out.println("Player Pos: " + position.xPos + "," + position.yPos);
    }
    var view = world.fetchGlobalResource(MainViewResource.class);
    view.mainView.setCenter(position.xPos*64, position.yPos*64);
  }


}
