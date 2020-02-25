package scc210game.game.states;

import org.jsfml.graphics.RenderWindow;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPop;
import scc210game.engine.state.trans.Transition;
import scc210game.game.resources.SavingGameResource;
import scc210game.game.states.events.ToggleSavingGameEvent;

import java.util.Set;

public class SavingGameState extends BaseInGameState {
    @Override
    public void onStart(World world) {
        world.addResource(new SavingGameResource());
        world.entityBuilder()
                .with(new Renderable(Set.of(ViewType.UI), 100, SavingGameState::render))
                .build();
        super.onStart(world);
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        var state = world.fetchResource(SavingGameResource.class);

        if (evt instanceof KeyPressedEvent) {
            KeyPressedEvent evt1 = (KeyPressedEvent) evt;
            state.handleInput(evt1.key, world);
        }

        if (evt instanceof ToggleSavingGameEvent) {
            return TransPop.getInstance();
        }

        return super.handleEvent(evt, world);
    }

    private static void render(Entity e, RenderWindow rw, World w) {
        var state = w.fetchResource(SavingGameResource.class);

        // TODO: render this
    }
}
