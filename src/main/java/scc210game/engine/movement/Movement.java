package scc210game.engine.movement;

import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.ECS;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.game.map.Map;
import scc210game.game.map.Player;
import scc210game.engine.audio.Audio;
import scc210game.game.map.Tile;
import scc210game.game.states.events.TriggerChestEvent;
import scc210game.game.states.events.TriggerCombatEvent;
import scc210game.game.states.events.TriggerStoryEvent;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

/**
 * Movement System that handles JSFML key events and alters
 * position of player coordinates accordingly
 */
public class Movement implements System {
  private final EventQueueReader eventReader;
  private int stepCount = 5;
  private int oldStepCount = 0;

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
            au.playSound("./src/main/resources/sounds/walking.wav", false);
          }
            checkSurrounding(world, map, position.xPos, position.yPos);
          break;
        }
        case S: {
          if(!map.getTile(position.xPos, position.yPos+1).hasCollision()) {
            position.yPos += 1;
            au.playSound("./src/main/resources/sounds/walking.wav", false);
          }
            checkSurrounding(world, map, position.xPos, position.yPos);
          break;
        }
        case D: {
          if(!map.getTile(position.xPos+1, position.yPos).hasCollision()) {
            position.xPos += 1;
            au.playSound("./src/main/resources/sounds/walking.wav", false);
          }
            checkSurrounding(world, map, position.xPos, position.yPos);
          break;
        }
        case W: {
          if(!map.getTile(position.xPos, position.yPos-1).hasCollision()) {
            position.yPos -= 1;
            au.playSound("./src/main/resources/sounds/walking.wav", false);

          }
            checkSurrounding(world, map, position.xPos, position.yPos);
          break;
        }
      }
      stepCount++;
    }
    var view = world.fetchGlobalResource(MainViewResource.class);
    view.mainView.setCenter(position.xPos*64, position.yPos*64);
  }

  /**
   * Method that checks the surrounding tiles for presence of a chest, enemy, or NPC
   * @param world to trigger events for the whole game
   * @param map entity to get the tiles
   * @param x coordinate of the player
   * @param y coordinate of the player
   */
    public void checkSurrounding(World world, Map map, int x, int y) {
      if(stepCount > oldStepCount+4) {
        Tile[] tiles = new Tile[4];
        tiles[0] = map.getTile(x + 1, y);
        tiles[1] = map.getTile(x - 1, y);
        tiles[2] = map.getTile(x, y + 1);
        tiles[3] = map.getTile(x, y - 1);

        for (Tile t : tiles) {
          if (t.getHasEnemy()) {
            java.lang.System.out.println("Enemy nearby");
            world.ecs.acceptEvent(new TriggerCombatEvent());
            oldStepCount = stepCount;
            break;
          }
          else if (t.canHaveChest()) {
            java.lang.System.out.println("Chest nearby");
            world.ecs.acceptEvent(new TriggerChestEvent());
            oldStepCount = stepCount;
            break;
          }
          else if (t.canHaveStory()) {
            java.lang.System.out.println("NPC nearby");
            world.ecs.acceptEvent(new TriggerStoryEvent());
            oldStepCount = stepCount;
            break;
          }
        }
      }

    }




}
