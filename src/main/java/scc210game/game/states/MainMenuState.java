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
import scc210game.engine.utils.ResourceLoader;
import scc210game.game.spawners.ui.BackgroundSpawner;
import scc210game.game.states.events.QuitGameEvent;
import scc210game.game.states.events.StartGameEvent;

public class MainMenuState extends InputHandlingState {
    Audio au = new Audio();

    @Override
    public void onStart(World world) {
        this.au.playSound(ResourceLoader.resolve("sounds/love_from_afar.wav"), true);
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.18f, 0.2f, 0.05f, "Start Game",
                (Entity e, World w) -> world.ecs.acceptEvent(new StartGameEvent()))).build();
        world.entityBuilder().with(new ClickableTextBoxSpawner(0.2f, 0.26f, 0.2f, 0.05f, "Quit Game",
                (Entity e, World w) -> world.ecs.acceptEvent(new QuitGameEvent()))).build();

        //TODO: Create background here, custom texture
        world.entityBuilder().with(new BackgroundSpawner("menu.png")).build();
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof StartGameEvent) {
            this.au.stopSound();
            this.au.playSound(ResourceLoader.resolve("sounds/menuSelect.wav"), false);
            // transition to the main game state
            return new TransPush(new MainGameState());
        }

        if (evt instanceof QuitGameEvent) {
            return TransQuit.getInstance();
        }

        return super.handleEvent(evt, world);
    }
}
