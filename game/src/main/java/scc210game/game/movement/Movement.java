package scc210game.game.movement;

import org.jsfml.window.Keyboard;
import scc210game.engine.ecs.System;
import scc210game.engine.events.Event;
import scc210game.engine.ecs.ECS;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.state.event.KeyDepressedEvent;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.game.map.Player;
import scc210game.game.components.PlayerLocked;
import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Movement System that handles JSFML key events and alters
 * position of player coordinates accordingly
 */
public class Movement implements System {
  private final EventQueueReader eventReader;
  private final Set<Keyboard.Key> pressedKeys;

  public Movement(ECS ecs) {
    this.eventReader = ecs.eventQueue.makeReader();
    ecs.eventQueue.listen(this.eventReader, KeyPressedEvent.class);
    ecs.eventQueue.listen(this.eventReader, KeyDepressedEvent.class);

    this.pressedKeys = new HashSet<>();
  }




  @Override
  public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
    for (Iterator<Event> it = world.ecs.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
      Event e = it.next();

      if (e instanceof KeyPressedEvent) {
        KeyPressedEvent e1 = (KeyPressedEvent) e;
        this.pressedKeys.add(e1.key);
      } else if (e instanceof KeyDepressedEvent) {
        KeyDepressedEvent e1 = (KeyDepressedEvent) e;
        this.pressedKeys.remove(e1.key);
      }
    }

    var playerEntO = world.applyQuery(Query.builder().require(Player.class).build()).findFirst();
    if (playerEntO.isEmpty())
      return;
    var playerEnt = playerEntO.get();
    var velocity = world.fetchComponent(playerEnt, Velocity.class);
    var positionLocked = world.fetchComponent(playerEnt, PlayerLocked.class);

    if (this.pressedKeys.contains(Keyboard.Key.A) && !positionLocked.locked)
      velocity.dx = -3;
    if (this.pressedKeys.contains(Keyboard.Key.S) && !positionLocked.locked)
      velocity.dy = 3;
    if (this.pressedKeys.contains(Keyboard.Key.D) && !positionLocked.locked)
      velocity.dx = 3;
    if (this.pressedKeys.contains(Keyboard.Key.W) && !positionLocked.locked)
      velocity.dy = -3;
  }





}
