package scc210game.game.systems;

import scc210game.engine.ecs.System;
import scc210game.engine.ecs.*;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.game.components.Dialogue;
import scc210game.game.events.DialogueCreateEvent;
import scc210game.game.spawners.ui.DialogueSpawner;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

public class DialogueHandlingSystem implements System {
    private final EventQueueReader eventReader;
    private final Query dialogueQuery = Query.builder().require(Dialogue.class).build();

    public DialogueHandlingSystem(ECS ecs) {
        this.eventReader = ecs.eventQueue.makeReader();
        ecs.eventQueue.listen(this.eventReader, DialogueCreateEvent.class);
        ecs.eventQueue.listen(this.eventReader, KeyPressedEvent.class);
    }


    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        // listen on both the world local event queue, and the global one

        for (Iterator<Event> it = world.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();
            this.handleEvent(world, e);
        }

        for (Iterator<Event> it = world.ecs.eventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
            Event e = it.next();
            this.handleEvent(world, e);
        }
    }

    private void handleEvent(World world, Event e) {
        if (e instanceof DialogueCreateEvent) {
            DialogueCreateEvent e1 = (DialogueCreateEvent) e;

            // mark all other dialogues as ignored
            this.ignoreExistingDialogues(world);

            // create the new dialogue
            world.entityBuilder().with(new DialogueSpawner(e1.message, e1.accept, e1.ignore)).build();
        } else if (e instanceof KeyPressedEvent) {
            KeyPressedEvent e1 = (KeyPressedEvent) e;

            switch (e1.key) {
                case Q: {
                    this.ignoreExistingDialogues(world);
                    break;
                }
                case RETURN: {
                    var entitiesToRemove = new ArrayList<Entity>();
                    var dialogueEntities = world.applyQuery(this.dialogueQuery).collect(Collectors.toList());
                    dialogueEntities.forEach(ent -> {
                        var dialogue = world.fetchComponent(ent, Dialogue.class);
                        dialogue.accept.accept(ent, world);
                        entitiesToRemove.add(ent);
                    });
                    for (final var ent : entitiesToRemove) {
                        world.removeEntity(ent);
                    }
                    break;
                }
            }
        }
    }

    private void ignoreExistingDialogues(World world) {
        var entitiesToRemove = new ArrayList<Entity>();
        var dialogueEntities = world.applyQuery(this.dialogueQuery).collect(Collectors.toList());
        dialogueEntities.forEach(ent -> {
            var dialogue = world.fetchComponent(ent, Dialogue.class);
            dialogue.ignore.accept(ent, world);
            entitiesToRemove.add(ent);
        });
        for (final var ent : entitiesToRemove) {
            world.removeEntity(ent);
        }
    }

}
