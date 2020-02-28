package scc210game.game.states;


import org.jsfml.window.Keyboard;
import scc210game.engine.audio.Audio;
import scc210game.engine.ecs.World;
import scc210game.engine.state.InputHandlingState;
import scc210game.engine.state.event.KeyDepressedEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPush;
import scc210game.engine.state.trans.TransReplaceAll;
import scc210game.engine.state.trans.Transition;
import scc210game.engine.utils.ResourceLoader;
import scc210game.game.states.events.ReturnToMainMenuEvent;
import scc210game.game.states.events.TogglePauseEvent;

/**
 * A base state for every state in our game, handles input and pausing etc
 */
public class BaseInGameState extends InputHandlingState {
    static {
        register(BaseInGameState.class, (j) -> new BaseInGameState());
    }

    Audio au = new Audio();

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        // pause on escape key press
        if (evt instanceof KeyDepressedEvent) {
            KeyDepressedEvent evt1 = (KeyDepressedEvent) evt;
            if (evt1.key == Keyboard.Key.ESCAPE) {
                world.ecs.acceptEvent(new TogglePauseEvent());
            }
        }

        if (evt instanceof TogglePauseEvent) {
            this.au.playSound(ResourceLoader.resolve("sounds/pause.wav"), false);
            return new TransPush(new PausedState());
        }

        if (evt instanceof ReturnToMainMenuEvent) {
            return new TransReplaceAll(new MainMenuState());
        }

        return super.handleEvent(evt, world);
    }
}
