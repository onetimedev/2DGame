package scc210game.ui.systems;

import org.jsfml.graphics.RenderWindow;
import scc210game.ecs.System;
import scc210game.ecs.*;
import scc210game.events.*;
import scc210game.state.event.InputEvent;
import scc210game.state.event.MouseButtonDepressedEvent;
import scc210game.state.event.MouseButtonPressedEvent;
import scc210game.state.event.MouseMovedEvent;
import scc210game.ui.components.UITransform;
import scc210game.utils.Tuple2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    private final RenderWindow window;
    private final EventQueueReader eventReader;
    private final Query uiEntityQuery = Query.builder()
            .require(UITransform.class)
            .build();

    public HandleInteraction(RenderWindow window) {
        this.window = window;
        this.eventReader = EventQueue.makeReader();
        EventQueue.listen(this.eventReader, InputEvent.class);
    }

    private Stream<Entity> getUIEntities(@Nonnull World world) {
        return world.applyQuery(this.uiEntityQuery);
    }

    private Optional<Tuple2<Entity, UITransform>> getEntityAtPosition(float x, float y, @Nonnull World world) {
        return this.getEntityAtPosition(x, y, world, null);
    }

    private Optional<Tuple2<Entity, UITransform>> getEntityAtPosition(float x, float y, @Nonnull World world, @Nullable Entity ignoring) {
        // map window coords to percentages
        var windowSize = this.window.getSize();

        var xPct = x / (float) windowSize.x;
        var yPct = y / (float) windowSize.y;

        return this.getUIEntities(world)
                .filter(e -> e != ignoring)
                .map(e -> new Tuple2<>(e, world.fetchComponent(e, UITransform.class)))
                .filter(t -> t.r.contains(xPct, yPct))
                .max(Comparator.comparing(t -> t.r.zPos));

    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = EventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();
            this.handleEvent(world, e);
        }
    }

    private void handleEvent(@Nonnull World world, Event e) {
        var state = world.fetchResource(InteractionState.class, () -> new InteractionState(false, null, null));

        if (e instanceof MouseButtonPressedEvent) {
            MouseButtonPressedEvent e1 = (MouseButtonPressedEvent) e;
            var entAtClickP = this.getEntityAtPosition(e1.x, e1.y, world);

            if (!entAtClickP.isPresent()) {
                return;
            }

            // begin 'dragging' this entity
            var entAtClick = entAtClickP.get();
            state.draggingEntityData = new DraggingData(entAtClick.l, e1.x, e1.y);
        } else if (e instanceof MouseButtonDepressedEvent) {
            // if dragging an entity, emit an EntityDropped, if not, emit an EntitiesClick
            MouseButtonDepressedEvent e1 = (MouseButtonDepressedEvent) e;

            if (state.draggingEntityData == null || (state.draggingEntityData.dragStartX == e1.x && state.draggingEntityData.dragStartY == e1.y)) {
                // wasn't dragging anything or the 'drag' wasn't a drag, emit a click
                var entAtClickP = this.getEntityAtPosition(e1.x, e1.y, world);

                // nothing to click on
                if (!entAtClickP.isPresent()) {
                    return;
                }

                var entAtClick = entAtClickP.get();
                Event evt = new EntitiesClickEvent(e1.x, e1.y, entAtClick.l);
                EventQueue.broadcast(evt);
            } else {
                var entAtDropP = this.getEntityAtPosition(e1.x, e1.y, world, state.draggingEntityData.draggingEntity);

                // nothing to drop on, emit failed drop
                if (!entAtDropP.isPresent()) {
                    Event evt = new EntitiesFailedDroppedEvent(state.draggingEntityData.draggingEntity);
                    EventQueue.broadcast(evt);
                } else {
                    var entAtDrop = entAtDropP.get();
                    Event evt = new EntitiesDroppedEvent(state.draggingEntityData.draggingEntity, entAtDrop.l);
                    EventQueue.broadcast(evt);
                }

                state.draggingEntityData = null;
            }
        } else if (e instanceof MouseMovedEvent) {
            MouseMovedEvent e1 = (MouseMovedEvent) e;

            // possible hire hover events

            var entAtHoverP = this.getEntityAtPosition(e1.x, e1.y, world,
                    state.draggingEntityData != null ?
                            state.draggingEntityData.draggingEntity :
                            null);

            if (entAtHoverP.isPresent()) {
                // we are hovering over a UI entity
                var entAtHover = entAtHoverP.get();

                if (entAtHover.l != state.hoveringEntity) {
                    // new hover or changed hovers

                    if (state.hoveringEntity != null) {
                        EventQueue.broadcast(new EntityHoverStopEvent(state.hoveringEntity));
                    }

                    EventQueue.broadcast(new EntityHoverStartEvent(entAtHover.l));
                }

                state.hoveringEntity = entAtHover.l;
            } else {
                // not hovering over anything, discard the old hover if there was one

                if (state.hoveringEntity != null) {
                    EventQueue.broadcast(new EntityHoverStopEvent(state.hoveringEntity));
                    state.hoveringEntity = null;
                }
            }

            // now go on to handling dragged entities

            if (state.draggingEntityData != null) {
                EventQueue.broadcast(new EntitiesDraggedEvent(
                        state.draggingEntityData.draggingEntity,
                        state.draggingEntityData.dragStartX,
                        state.draggingEntityData.dragStartY,
                        e1.dx,
                        e1.dy));
            }
        }
    }

    private static class DraggingData {
        final Entity draggingEntity;
        final float dragStartX;
        final float dragStartY;

        public DraggingData(Entity draggingEntity, float dragStartX, float dragStartY) {
            this.draggingEntity = draggingEntity;
            this.dragStartX = dragStartX;
            this.dragStartY = dragStartY;
        }
    }

    private static class InteractionState extends Resource {
        // NOTE: Might need some way of marking components as non-persistent
        static {
            register(InteractionState.class, s -> new InteractionState(false, null, null));
        }

        boolean isMouseDown;
        @Nullable
        DraggingData draggingEntityData;
        @Nullable
        Entity hoveringEntity;

        private InteractionState(boolean isMouseDown, @Nullable DraggingData draggingEntityData, @Nullable Entity hoveringEntity) {
            this.isMouseDown = isMouseDown;
            this.draggingEntityData = draggingEntityData;
            this.hoveringEntity = hoveringEntity;
        }

        @Override
        public String serialize() {
            return "{}";
        }
    }
}
