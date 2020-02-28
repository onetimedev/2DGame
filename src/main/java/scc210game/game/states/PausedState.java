package scc210game.game.states;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPop;
import scc210game.engine.state.trans.Transition;
import scc210game.engine.ui.spawners.ClickableTextBoxSpawner;
import scc210game.game.states.events.ReturnToMainMenuEvent;
import scc210game.game.states.events.TogglePauseEvent;

public class PausedState extends BaseInGameState {
    static {
        register(PausedState.class, (j) -> new PausedState());
    }

    private static void resumeButton(Entity e, World w) {
        w.ecs.acceptEvent(new TogglePauseEvent());
    }

    private static void mainMenuButton(Entity e, World w) {
        w.ecs.acceptEvent(new ReturnToMainMenuEvent());
    }

    @Override
    public void onStart(World world) {
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.1f, 0.6f, 0.1f, "Resume Game",
                PausedState::resumeButton)).build();
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.28f, 0.6f, 0.1f, "Main Menu",
                PausedState::mainMenuButton)).build();
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof TogglePauseEvent) {
            return TransPop.getInstance();
        }

        return super.handleEvent(evt, world);
    }
}
