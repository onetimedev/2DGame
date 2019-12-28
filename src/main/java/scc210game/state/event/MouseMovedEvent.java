package scc210game.state.event;

public class MouseMovedEvent implements InputEvent {
    public final int dx;
    public final int dy;

    public MouseMovedEvent(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
