package scc210game.ui.systems;

import scc210game.ecs.Entity;
import scc210game.ecs.Query;
import scc210game.ecs.System;
import scc210game.ecs.World;
import scc210game.events.EntitiesClickEvent;
import scc210game.events.Event;
import scc210game.events.EventQueue;
import scc210game.events.EventQueueReader;
import scc210game.state.State;
import scc210game.state.event.MouseButtonPressedEvent;
import scc210game.state.event.StateEvent;
import scc210game.ui.UITransform;
import scc210game.utils.Tuple2;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * The system to handle user interaction with UI components
 * <p>
 * This must be added to the ECS to work
 */
public class HandleInteraction implements System {
    private final EventQueueReader eventReader;
    private final Query uiEntityQuery = Query.builder()
            .require(UITransform.class)
            .build();

    public HandleInteraction() {
        this.eventReader = EventQueue.makeReader();
        EventQueue.listen(this.eventReader, StateEvent.class);
    }

    private Stream<Entity> getUIEntities(@Nonnull World world) {
        return world.applyQuery(this.uiEntityQuery);
    }

    private Optional<Tuple2<Entity, UITransform>> getEntityAtPosition(float x, float y, @Nonnull World world) {
        return this.getUIEntities(world)
                .map(e -> new Tuple2<>(e, world.fetchComponent(e, UITransform.class)))
                .filter(t -> t.r.contains(x, y))
                .max(Comparator.comparing(t -> t.r.zPos));

    }

    @Override
    public void run(@Nonnull World world, @Nonnull Class<? extends State> currentState, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = EventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();

            // TODO: Currently we just fire off click events whenever the mouse button is pressed down
            //      That's kinda meh, so we need to keep track of mouse interaction and figure out when
            //      to fire off mouse clicks/ drags based on when the mouse is pressed, moved, and released
            if (e instanceof MouseButtonPressedEvent) {
                MouseButtonPressedEvent e1 = (MouseButtonPressedEvent) e;
                var entAtClickP = this.getEntityAtPosition(e1.x, e1.y, world);

                if (!entAtClickP.isPresent()) {
                    continue;
                }

                var entAtClick = entAtClickP.get();
                Event evt = new EntitiesClickEvent(e1.x, e1.y, entAtClick.l);
                EventQueue.broadcast(evt);
            }
        }
    }
}
