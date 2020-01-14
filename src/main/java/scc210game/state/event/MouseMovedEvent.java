package scc210game.state.event;

public class MouseMovedEvent implements InputEvent {
    public final float dx;
    public final float dy;

    public MouseMovedEvent(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
