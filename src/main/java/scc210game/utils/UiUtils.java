package scc210game.utils;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

public class UiUtils {
    public static Vector2f convertUiSize(RenderWindow w, Vector2f uiSize) {
        return convertUiSize(w.getView().getSize(), uiSize);
    }

    public static Vector2f convertUiSize(Vector2f windowSize, Vector2f uiSize) {
        return new Vector2f(windowSize.x * uiSize.x, windowSize.y * uiSize.y);
    }

    public static Color transformColor(java.awt.Color c) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }
}
