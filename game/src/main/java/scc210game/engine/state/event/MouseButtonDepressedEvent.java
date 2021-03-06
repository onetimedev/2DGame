package scc210game.engine.state.event;

import org.jsfml.window.Mouse;

public class MouseButtonDepressedEvent extends InputEvent {
    public final float x;
    public final float y;
    public final Mouse.Button button;

    public MouseButtonDepressedEvent(float x, float y, Mouse.Button button) {
        this.x = x;
        this.y = y;
        this.button = button;
    }
}
