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
import scc210game.game.components.HasToolTip;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Iterator;

public class ToolTipHandler implements System {
    private final EventQueueReader eventReader;

    public ToolTipHandler(ECS ecs) {
        this.eventReader = ecs.eventQueue.makeReader();
        ecs.eventQueue.listen(this.eventReader, EntityHoverStartEvent.class);
        ecs.eventQueue.listen(this.eventReader, EntityHoverStopEvent.class);
    }

    private static void enterCompletionCallback(Entity e, World w) {
        var hoverInfo = w.fetchComponent(e, HasToolTip.class);
        hoverInfo.hoverState = HasToolTip.HoverState.HOVERED;
    }

    private static void leaveCompletionCallback(Entity e, World w) {
        var hoverInfo = w.fetchComponent(e, HasToolTip.class);
        hoverInfo.hoverState = HasToolTip.HoverState.NOTHOVERED;
    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = world.ecs.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();

            if (e instanceof EntityHoverStartEvent) {
                EntityHoverStartEvent e1 = (EntityHoverStartEvent) e;
                if (!world.hasComponent(e1.entity, HasToolTip.class))
                    continue;

                var toolTip = world.fetchComponent(e1.entity, HasToolTip.class);

                switch (toolTip.hoverState) {
                    case NOTHOVERED: {
                        // not hovered, start fading in
                        toolTip.hoverState = HasToolTip.HoverState.ENTERING;
                        world.addComponentToEntity(e1.entity, new Animate(Duration.ofMillis(300),
                                ToolTipHandler::enterCompletionCallback));
                        break;
                    }
                    case ENTERING:
                    case HOVERED:
                        // already entering? do nothing
                        break;
                    case LEAVING: {
                        // is leaving, start fading in from the current position
                        var animation = world.fetchComponent(e1.entity, Animate.class);
                        toolTip.hoverState = HasToolTip.HoverState.ENTERING;
                        world.removeComponentFromEntity(e1.entity, Animate.class);
                        world.addComponentToEntity(e1.entity, new Animate(Duration.ofMillis(300),
                                1.0f - animation.pctComplete,
                                ToolTipHandler::enterCompletionCallback));
                        break;
                    }
                }
            } else if (e instanceof EntityHoverStopEvent) {
                EntityHoverStopEvent e1 = (EntityHoverStopEvent) e;

                if (!world.hasComponent(e1.entity, HasToolTip.class))
                    continue;

                var toolTip = world.fetchComponent(e1.entity, HasToolTip.class);

                switch (toolTip.hoverState) {
                    case ENTERING: {
                        // was entering, start fading out from current position
                        var animation = world.fetchComponent(e1.entity, Animate.class);
                        toolTip.hoverState = HasToolTip.HoverState.LEAVING;
                        world.removeComponentFromEntity(e1.entity, Animate.class);
                        world.addComponentToEntity(e1.entity, new Animate(Duration.ofMillis(300),
                                1.0f - animation.pctComplete,
                                ToolTipHandler::leaveCompletionCallback));
                        break;
                    }
                    case HOVERED: {
                        // leaving once we've been hovered, just enter leave state and animate
                        toolTip.hoverState = HasToolTip.HoverState.LEAVING;
                        world.addComponentToEntity(e1.entity, new Animate(Duration.ofMillis(300),
                                ToolTipHandler::leaveCompletionCallback));
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