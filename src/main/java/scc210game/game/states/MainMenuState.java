package scc210game.game.states;

import scc210game.engine.audio.Audio;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.state.InputHandlingState;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPush;
import scc210game.engine.state.trans.TransQuit;
import scc210game.engine.state.trans.Transition;
import scc210game.engine.ui.spawners.ClickableTextBoxSpawner;
import scc210game.game.states.events.QuitGameEvent;
import scc210game.game.states.events.StartGameEvent;

public class MainMenuState extends InputHandlingState {
    Audio au = new Audio();

    @Override
    public void onStart(World world) {
        au.playSound("./src/main/resources/sounds/311 - Love From Afar.wav", true);
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.1f, 0.6f, 0.1f, "Start Game",
                (Entity e, World w) -> world.ecs.acceptEvent(new StartGameEvent()))).build();
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.28f, 0.6f, 0.1f, "Quit Game",
                (Entity e, World w) -> world.ecs.acceptEvent(new QuitGameEvent()))).build();
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof StartGameEvent) {
            au.stopSound();
            au.playSound("./src/main/resources/sounds/menuSelect.wav", false);
            // transition to the main game state
            return new TransPush(new MainGameState());
        }

        if (evt instanceof QuitGameEvent) {
            return TransQuit.getInstance();
        }

        return super.handleEvent(evt, world);
    }
}
