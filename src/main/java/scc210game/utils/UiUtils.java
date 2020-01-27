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
     *
     * @param c the {@link java.awt.Color} input colour
     * @return the {@link Color} transformed colour
     */
    public static Color transformColor(java.awt.Color c) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    /**
     * Correct a size assuming a 1:1 aspect ratio into one that works for 16:9 aspect ratio
     *
     * @param values the size assuming a 1:1 aspect ratio
     * @return a size that has the same ratio for 16:9 aspect ratio
     */
    public static Vector2f correctAspectRatio(Vector2f values) {
        var aspectRatio = 16f / 9f;

        return new Vector2f(values.x, values.y * aspectRatio);
    }

    /**
     * Get a position that will have the given size object appear at the center of the given targetPos and targetSize
     *
     * @param size       size of rectangle to center
     * @param targetPos  position of rectangle to center on
     * @param targetSize size of rectangle to center on
     * @return position needed to have the given size rectangle appear centered
     */
    public static Vector2f centerTransforms(Vector2f size, Vector2f targetPos, Vector2f targetSize) {
        var leftBorder = (targetSize.x - size.x) / 2f;
        var topBorder = (targetSize.y - size.y) / 2f;

        return new Vector2f(targetPos.x + leftBorder, targetPos.y + topBorder);
    }
}
