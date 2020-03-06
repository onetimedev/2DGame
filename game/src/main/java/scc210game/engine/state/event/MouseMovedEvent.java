package scc210game.engine.state.event;

public class MouseMovedEvent extends InputEvent {
    /**
     * The x position of the mouse (in percentage of screen size)
     */
    public final float x;

    /**
     * The y position of the mouse (in percentage of screen size)
     */
    public final float y;

    /**
     * The change in x position of the mouse since the last move event (in percentage of screen size)
     */
    public final float dx;

    /**
     * The change in y position of the mouse since the last move event (in percentage of screen size)
     */
    public final float dy;

    public MouseMovedEvent(float x, float y, float dx, float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }
}
