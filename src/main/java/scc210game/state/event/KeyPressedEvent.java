package scc210game.state.event;

import org.jsfml.window.Keyboard;

public class KeyPressedEvent extends InputEvent {
    public final Keyboard.Key key;

    public KeyPressedEvent(Keyboard.Key key) {
        this.key = key;
    }
}
