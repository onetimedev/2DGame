package scc210game.engine.movement;

import org.jsfml.window.Keyboard;
import scc210game.engine.ecs.*;
import scc210game.engine.ecs.System;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.game.map.Player;
import scc210game.engine.audio.Audio;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

/**
 * Movement System that handles JSFML key events and alters
 * position of player coordinates accordingly
 */
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
      this.handleMovement(world, e);
    }
  }


  private void handleMovement(World world, Event e) {
    var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
    if (!playerEntO.isPresent())
      return;

    var playerEnt = playerEntO.get();
    var velocity = world.fetchComponent(playerEnt, Velocity.class);

    if (e instanceof KeyPressedEvent) {
      KeyPressedEvent type = (KeyPressedEvent) e;

      if(type.key.equals(Keyboard.Key.A))
        velocity.dx = -3;
      if(type.key.equals(Keyboard.Key.S))
        velocity.dy = 3;
      if(type.key.equals(Keyboard.Key.D))
        velocity.dx = 3;
      if(type.key.equals(Keyboard.Key.W))
        velocity.dy = -3;

    }
  }

  /*
  @Override
  public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
    var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
    if (!playerEntO.isPresent())
      return;
    var playerEnt = playerEntO.get();
    var velocity = world.fetchComponent(playerEnt, Velocity.class);

    if(Keyboard.isKeyPressed(Keyboard.Key.A))
      velocity.dx = -3;
    if(Keyboard.isKeyPressed(Keyboard.Key.S))
      velocity.dy = 3;
    if(Keyboard.isKeyPressed(Keyboard.Key.D))
      velocity.dx = 3;
    if(Keyboard.isKeyPressed(Keyboard.Key.W))
      velocity.dy = -3;
  }


*/


}
