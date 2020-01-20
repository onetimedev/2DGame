package scc210game.utils;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

public class UiUtils {
    /**
     * Helper method to scale a 'percentage of screen' size into an actual size
     * @param w the RenderWindow to scale to
     * @param uiSize a vector containing the width and height to scale from
     * @return A new vector containing the scaled values
     */
    public static Vector2f convertUiSize(RenderWindow w, Vector2f uiSize) {
        return convertUiSize(w.getView().getSize(), uiSize);
    }

    /**
     * Helper method to scale a 'percentage of screen' size into an actual size
     * @param windowSize the window size to scale to
     * @param uiSize a vector containing the width and height to scale from
     * @return A new vector containing the scaled values
     */
    public static Vector2f convertUiSize(Vector2f windowSize, Vector2f uiSize) {
        return new Vector2f(windowSize.x * uiSize.x, windowSize.y * uiSize.y);
    }

    /**
     * Helper method to translate a 'percentage of screen' position into an actual position
     * @param w the RenderWindow to translate to
     * @param uiSize a vector containing the x and y coordinates to translate from
     * @return A new vector containing the translated coordinates
     */
    public static Vector2f convertUiPosition(RenderWindow w, Vector2f uiSize) {
        return convertUiPosition(w.getView().getSize(), uiSize);
    }

    /**
     * Helper method to translate a 'percentage of screen' position into an actual position
     * @param windowSize the window size to translate to
     * @param uiSize a vector containing the x and y coordinates to translate from
     * @return A new vector containing the translated coordinates
     */
    public static Vector2f convertUiPosition(Vector2f windowSize, Vector2f uiSize) {
        var x = uiSize.x - 0.5f;
        var y = uiSize.y - 0.5f;
        return new Vector2f(windowSize.x * x, windowSize.y * y);
    }

    /**
     * Transform a {@link java.awt.Color} colour into a {@link Color} colour.
     * @param c the {@link java.awt.Color} input colour
     * @return the {@link Color} transformed colour
     */
    public static Color transformColor(java.awt.Color c) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }
}
