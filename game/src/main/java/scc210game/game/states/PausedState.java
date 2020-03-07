package scc210game.game.states;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPop;
import scc210game.engine.state.trans.TransPush;
import scc210game.engine.state.trans.Transition;
import scc210game.game.spawners.ClickableTextBoxSpawner;
import scc210game.game.spawners.ui.BackgroundSpawner;
import scc210game.game.states.events.ReturnToMainMenuEvent;
import scc210game.game.states.events.TogglePauseEvent;
import scc210game.game.states.events.ToggleSavingGameEvent;

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

    private static void saveGameButton(Entity e, World w) {
        w.ecs.acceptEvent(new ToggleSavingGameEvent());
    }

    @Override
    public void onStart(World world) {
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.22f, 0.2f, 0.05f, "Resume Game",
                PausedState::resumeButton)).build();

        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.30f, 0.2f, 0.05f, "Main Menu",
                PausedState::mainMenuButton)).build();

        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.38f, 0.2f, 0.05f, "Save Game",
                PausedState::saveGameButton)).build();

        world.entityBuilder().with(new BackgroundSpawner("pause.png")).build();
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof TogglePauseEvent) {
            return TransPop.getInstance();
        }

        if (evt instanceof ToggleSavingGameEvent) {
            return new TransPush(new SavingGameState());
        }

        return super.handleEvent(evt, world);
    }
}
