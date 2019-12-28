package scc210game.state.event;

import org.jsfml.window.Mouse;

public class MouseButtonDepressedEvent {
    public final Mouse.Button button;

    public MouseButtonDepressedEvent(Mouse.Button button) {
        this.button = button;
    }
}
