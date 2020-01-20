package scc210game.state.event;

public class MouseMovedEvent implements InputEvent {
    /**
     * The x position of the mouse
     */
    public final float x;

    /**
     * The y position of the mouse
     */
    public final float y;

    /**
     * The change in x position of the mouse since the last move event
     */
    public final float dx;

    /**
     * The change in y position of the mouse since the last move event
     */
    public final float dy;

    public MouseMovedEvent(float x, float y, float dx, float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }
}
