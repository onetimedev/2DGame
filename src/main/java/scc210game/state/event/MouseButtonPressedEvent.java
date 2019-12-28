package scc210game.state.event;

import org.jsfml.window.Mouse;

public class MouseButtonPressedEvent {
    public final Mouse.Button button;

    public MouseButtonPressedEvent(Mouse.Button button) {
        this.button = button;
    }
}
