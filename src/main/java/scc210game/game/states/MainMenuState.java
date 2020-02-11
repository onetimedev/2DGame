package scc210game.game.states;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.state.InputHandlingState;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPush;
import scc210game.engine.state.trans.TransQuit;
import scc210game.engine.state.trans.Transition;
import scc210game.engine.ui.spawners.ClickableTextBoxSpawner;
import scc210game.game.states.events.CombatStateEvent;
import scc210game.game.states.events.QuitGameEvent;
import scc210game.game.states.events.StartGameEvent;

public class MainMenuState extends InputHandlingState {
    @Override
    public void onStart(World world) {
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.1f, 0.6f, 0.1f, "Start Game",
                (Entity e, World w) -> world.ecs.acceptEvent(new StartGameEvent()))).build();
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.28f, 0.6f, 0.1f, "Quit Game",
                (Entity e, World w) -> world.ecs.acceptEvent(new QuitGameEvent()))).build();
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.42f, 0.6f, 0.1f, "Test Combat",
                (Entity e, World w) -> world.ecs.acceptEvent(new CombatStateEvent()))).build();
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof StartGameEvent) {
            // transition to the main game state
            return new TransPush(new MainGameState());
        }

        if (evt instanceof QuitGameEvent) {
            return TransQuit.getInstance();
        }

        if(evt instanceof CombatStateEvent){
            return new TransPush(new CombatState());
        }

        return super.handleEvent(evt, world);
    }
}
