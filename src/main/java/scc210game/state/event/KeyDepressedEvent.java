package scc210game.state.event;

import org.jsfml.window.Keyboard;

public class KeyDepressedEvent implements InputEvent {
    public final Keyboard.Key key;

    public KeyDepressedEvent(Keyboard.Key key) {
        this.key = key;
    }
}
