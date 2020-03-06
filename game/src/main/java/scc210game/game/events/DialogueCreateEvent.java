package scc210game.game.events;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.events.Event;
import scc210game.engine.utils.SerializableBiConsumer;

import javax.annotation.Nonnull;

public class DialogueCreateEvent extends Event {
    @Nonnull public final String message;
    @Nonnull public final SerializableBiConsumer<Entity, World> accept;
    @Nonnull public final SerializableBiConsumer<Entity, World> ignore;

    public DialogueCreateEvent(@Nonnull String message, @Nonnull SerializableBiConsumer<Entity, World> accept, @Nonnull SerializableBiConsumer<Entity, World> ignore) {
        this.message = message;
        this.accept = accept;
        this.ignore = ignore;
    }
}
