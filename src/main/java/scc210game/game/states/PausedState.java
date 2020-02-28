package scc210game.game.states;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPop;
import scc210game.engine.state.trans.Transition;
import scc210game.engine.ui.spawners.ClickableTextBoxSpawner;
import scc210game.game.spawners.ui.BackgroundSpawner;
import scc210game.game.states.events.ReturnToMainMenuEvent;
import scc210game.game.states.events.TogglePauseEvent;

public class PausedState extends BaseInGameState {

    @Override
    public void onStart(World world) {
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.18f, 0.2f, 0.05f, "Resume Game",
                (Entity e, World w) -> world.ecs.acceptEvent(new TogglePauseEvent()))).build();
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.26f, 0.2f, 0.05f, "Main Menu",
                (Entity e, World w) -> world.ecs.acceptEvent(new ReturnToMainMenuEvent()))).build();

        //TODO: Create background here, custom texture
        world.entityBuilder().with(new BackgroundSpawner("menu.png")).build();
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof TogglePauseEvent) {
            return TransPop.getInstance();
        }

        return super.handleEvent(evt, world);
    }
}
