package scc210game.engine.state.event;

import org.jsfml.window.Keyboard;

public class KeyDepressedEvent extends InputEvent {
    public final Keyboard.Key key;

    public KeyDepressedEvent(Keyboard.Key key) {
        this.key = key;
    }
}
