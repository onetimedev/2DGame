package scc210game.state.event;

import org.jsfml.window.Mouse;

public class MouseButtonPressedEvent {
    public final float x;
    public final float y;
    public final Mouse.Button button;

    public MouseButtonPressedEvent(float x, float y, Mouse.Button button) {
        this.x = x;
        this.y = y;
        this.button = button;
    }
}
