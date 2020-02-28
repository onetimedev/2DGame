package scc210game.engine.ui.systems;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.*;
import scc210game.engine.events.*;
import scc210game.engine.state.event.InputEvent;
import scc210game.engine.state.event.MouseButtonDepressedEvent;
import scc210game.engine.state.event.MouseButtonPressedEvent;
import scc210game.engine.state.event.MouseMovedEvent;
import scc210game.engine.ui.components.UIClickable;
import scc210game.engine.ui.components.UIDraggable;
import scc210game.engine.ui.components.UIDroppable;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.Tuple2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;


/*
 * NOTE: all x/y values in this class are in percentages of the screen
 */

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

    private final Query draggableUIEntityQuery = Query.builder()
            .require(UITransform.class)
            .require(UIDraggable.class)
            .build();

    private final Query droppableUIEntityQuery = Query.builder()
            .require(UITransform.class)
            .require(UIDroppable.class)
            .build();

    private final Query clickableUIEntityQuery = Query.builder()
            .require(UITransform.class)
            .require(UIClickable.class)
            .build();

    public HandleInteraction(ECS ecs) {
        this.eventReader = ecs.eventQueue.makeReader();
        ecs.eventQueue.listen(this.eventReader, InputEvent.class);
    }

    private Optional<Tuple2<Entity, UITransform>> getEntityAtPosition(float x, float y, @Nonnull World world, Query q) {
        return this.getEntityAtPosition(x, y, world, q, null);
    }

    private Optional<Tuple2<Entity, UITransform>> getEntityAtPosition(float x, float y, @Nonnull World world, Query q, @Nullable Entity ignoring) {
        return world.applyQuery(q)
                .filter(e -> e != ignoring)
                .map(e -> new Tuple2<>(e, world.fetchComponent(e, UITransform.class)))
                .filter(t -> t.r.contains(x, y))
                .max(Comparator.comparing(t -> t.r.zPos));

    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (Iterator<Event> it = world.ecs.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();
            this.handleEvent(world, e);
        }
    }

    private void handleEvent(@Nonnull World world, Event e) {
        var state = world.fetchResource(InteractionState.class, () -> new InteractionState(false, null, null));

        if (e instanceof MouseButtonPressedEvent) {
            MouseButtonPressedEvent e1 = (MouseButtonPressedEvent) e;
            var entAtClickP = this.getEntityAtPosition(e1.x, e1.y, world, this.uiEntityQuery);

            if (!entAtClickP.isPresent()) {
                return;
            }

            var entAtClick = entAtClickP.get();

            if (world.hasComponent(entAtClick.l, UIDraggable.class)) {
                // begin 'dragging' this entity, if it can be dragged
                state.draggingEntityData = new DraggingData(entAtClick.l, e1.x, e1.y);
            }
        } else if (e instanceof MouseButtonDepressedEvent) {
            // if dragging an entity, emit an EntityDropped, if not, emit an EntitiesClick
            MouseButtonDepressedEvent e1 = (MouseButtonDepressedEvent) e;

            if (state.draggingEntityData == null || (state.draggingEntityData.dragStartX == e1.x && state.draggingEntityData.dragStartY == e1.y)) {
                state.draggingEntityData = null;
                // wasn't dragging anything or the 'drag' wasn't a drag, emit a click
                var entAtClickP = this.getEntityAtPosition(e1.x, e1.y, world, this.clickableUIEntityQuery);

                // nothing to click on
                if (!entAtClickP.isPresent()) {
                    return;
                }

                var entAtClick = entAtClickP.get();

                if (world.hasComponent(entAtClick.l, UIClickable.class)) {
                    Event evt = new EntityClickEvent(e1.x, e1.y, entAtClick.l);
                    world.ecs.eventQueue.broadcast(evt);
                }
            } else {
                var entAtDropP = this.getEntityAtPosition(e1.x, e1.y, world, this.droppableUIEntityQuery, state.draggingEntityData.draggingEntity);

                if (!entAtDropP.isPresent()) {
                    Event evt = new EntityFailedDroppedEvent(state.draggingEntityData.draggingEntity,
                            state.draggingEntityData.dragStartX,
                            state.draggingEntityData.dragStartY,
                            state.draggingEntityData.lastXPosition - state.draggingEntityData.dragStartX,
                            state.draggingEntityData.lastYPosition - state.draggingEntityData.dragStartY);
                    world.ecs.eventQueue.broadcast(evt);
                } else {
                    var entAtDrop = entAtDropP.get();
                    Event evt = new EntityDroppedEvent(state.draggingEntityData.draggingEntity, entAtDrop.l,
                            state.draggingEntityData.dragStartX,
                            state.draggingEntityData.dragStartY,
                            state.draggingEntityData.lastXPosition - state.draggingEntityData.dragStartX,
                            state.draggingEntityData.lastYPosition - state.draggingEntityData.dragStartY);
                    world.ecs.eventQueue.broadcast(evt);
                }

                state.draggingEntityData = null;
            }
        } else if (e instanceof MouseMovedEvent) {
            MouseMovedEvent e1 = (MouseMovedEvent) e;

            var entAtHoverP = this.getEntityAtPosition(e1.x, e1.y, world, this.uiEntityQuery,
                    state.draggingEntityData != null ?
                            state.draggingEntityData.draggingEntity :
                            null);

            if (entAtHoverP.isPresent()) {
                // we are hovering over a UI entity
                var entAtHover = entAtHoverP.get();

                if (entAtHover.l != state.hoveringEntity) {
                    // new hover or changed hovers

                    if (state.hoveringEntity != null) {
                        world.ecs.eventQueue.broadcast(new EntityHoverStopEvent(state.hoveringEntity));
                    }

                    world.ecs.eventQueue.broadcast(new EntityHoverStartEvent(entAtHover.l));
                }

                state.hoveringEntity = entAtHover.l;
            } else {
                // not hovering over anything, discard the old hover if there was one

                if (state.hoveringEntity != null) {
                    world.ecs.eventQueue.broadcast(new EntityHoverStopEvent(state.hoveringEntity));
                    state.hoveringEntity = null;
                }
            }

            // now go on to handling dragged entities

            if (state.draggingEntityData != null) {
                world.ecs.eventQueue.broadcast(new EntityDraggedEvent(
                        state.draggingEntityData.draggingEntity,
                        state.draggingEntityData.dragStartX,
                        state.draggingEntityData.dragStartY,
                        e1.x - state.draggingEntityData.dragStartX,
                        e1.y - state.draggingEntityData.dragStartY,
                        e1.dx, e1.dy));
                state.draggingEntityData.lastXPosition = e1.x;
                state.draggingEntityData.lastYPosition = e1.y;
            }
        }
    }

    private static class DraggingData {
        final Entity draggingEntity;
        final float dragStartX;
        final float dragStartY;
        float lastXPosition;
        float lastYPosition;

        public DraggingData(Entity draggingEntity, float dragStartX, float dragStartY) {
            this.draggingEntity = draggingEntity;
            this.dragStartX = dragStartX;
            this.dragStartY = dragStartY;
            this.lastXPosition = dragStartX;
            this.lastYPosition = dragStartY;
        }
    }

    private static class InteractionState extends Resource {
        // NOTE: Might need some way of marking components as non-persistent
        static {
            register(InteractionState.class, s -> new InteractionState(false, null, null));
        }

        @Override
        public boolean shouldKeep() {
            return false;
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
        public Jsonable serialize() {
            return new JsonObject();
        }
    }
}
