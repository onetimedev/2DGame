package scc210game.game.systems;

import scc210game.engine.animation.Animate;
import scc210game.engine.ecs.ECS;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.events.EntityHoverStartEvent;
import scc210game.engine.events.EntityHoverStopEvent;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.game.components.HoverInfo;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

public class HoverSpawner implements System {
    private final EventQueueReader eventReader;

    public HoverSpawner(ECS ecs) {
        this.eventReader = ecs.eventQueue.makeReader();
        ecs.eventQueue.listen(this.eventReader, EntityHoverStartEvent.class);
        ecs.eventQueue.listen(this.eventReader, EntityHoverStopEvent.class);
    }

    private static void enterCompletionCallback(Entity e, World w) {
        var hoverInfo = w.fetchComponent(e, HoverInfo.class);
        hoverInfo.hoverState = HoverInfo.HoverInfoState.HOVERED;
    }

    private static void leaveCompletionCallback(Entity e, World w) {
        var hoverInfo = w.fetchComponent(e, HoverInfo.class);
        hoverInfo.hoverState = HoverInfo.HoverInfoState.NOTHOVERED;
    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = world.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();

            if (e instanceof EntityHoverStartEvent) {
                EntityHoverStartEvent e1 = (EntityHoverStartEvent) e;
                if (!world.hasComponent(e1.entity, HoverInfo.class))
                    continue;

                var hoverInfo = world.fetchComponent(e1.entity, HoverInfo.class);

                switch (hoverInfo.hoverState) {
                    case NOTHOVERED: {
                        // not hovered, start fading in
                        hoverInfo.hoverState = HoverInfo.HoverInfoState.ENTERING;
                        world.addComponentToEntity(e1.entity, new Animate(Duration.ofMillis(300),
                                HoverSpawner::enterCompletionCallback));
                        break;
                    }
                    case ENTERING:
                    case HOVERED:
                        // already entering? do nothing
                        break;
                    case LEAVING: {
                        // is leaving, start fading in from the current position
                        var animation = world.fetchComponent(e1.entity, Animate.class);
                        hoverInfo.hoverState = HoverInfo.HoverInfoState.ENTERING;
                        world.removeComponentFromEntity(e1.entity, Animate.class);
                        world.addComponentToEntity(e1.entity, new Animate(Duration.ofMillis(300),
                                1.0f - animation.pctComplete,
                                HoverSpawner::enterCompletionCallback));
                        break;
                    }
                }
            } else if (e instanceof EntityHoverStopEvent) {
                EntityHoverStopEvent e1 = (EntityHoverStopEvent) e;

                if (!world.hasComponent(e1.entity, HoverInfo.class))
                    continue;

                var hoverInfo = world.fetchComponent(e1.entity, HoverInfo.class);

                switch (hoverInfo.hoverState) {
                    case ENTERING: {
                        // was entering, start fading out from current position
                        var animation = world.fetchComponent(e1.entity, Animate.class);
                        hoverInfo.hoverState = HoverInfo.HoverInfoState.LEAVING;
                        world.removeComponentFromEntity(e1.entity, Animate.class);
                        world.addComponentToEntity(e1.entity, new Animate(Duration.ofMillis(300),
                                1.0f - animation.pctComplete,
                                HoverSpawner::leaveCompletionCallback));
                        break;
                    }
                    case HOVERED: {
                        // leaving once we've been hovered, just enter leave state and animate
                        hoverInfo.hoverState = HoverInfo.HoverInfoState.LEAVING;
                        world.addComponentToEntity(e1.entity, new Animate(Duration.ofMillis(300),
                                HoverSpawner::leaveCompletionCallback));
                        break;
                    }
                    case NOTHOVERED:
                    case LEAVING:
                        // already leaving or not hovered? do nothing
                        break;
                }
            }
        }
    }
}