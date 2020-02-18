package scc210game.game.events;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.events.Event;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class DialogueCreateEvent extends Event {
    @Nonnull public final String message;
    @Nonnull public final BiConsumer<Entity, World> accept;
    @Nonnull public final BiConsumer<Entity, World> ignore;

    public DialogueCreateEvent(@Nonnull String message, @Nonnull BiConsumer<Entity, World> accept, @Nonnull BiConsumer<Entity, World> ignore) {
        this.message = message;
        this.accept = accept;
        this.ignore = ignore;
    }
}
