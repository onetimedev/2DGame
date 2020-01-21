package scc210game.render;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;
import scc210game.ecs.ECS;
import scc210game.state.event.StateEvent;
import scc210game.utils.Tuple2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Singleton class to hold game loop, take entities to render, and change mainWindow views
 */
public class EngineSetup {
    private static final int width = 720;
    private static final int height = 480;

    private final RenderWindow mainWindow;
    private final Map<ViewType, Tuple2<FloatRect, View>> views;
    private final ECS ecs;

    private EngineSetup() {
        this.mainWindow = new RenderWindow();
        this.mainWindow.create(new VideoMode(EngineSetup.width, EngineSetup.height), "SCC210 Game");

        this.mainWindow.setFramerateLimit(60);

        this.views = new HashMap<>() {{
            this.put(ViewType.MAIN, new Tuple2<>(
                    new FloatRect(0f, 0f, 1f, 1f),
                    new View(new Vector2f(0, 0), new Vector2f(EngineSetup.width, EngineSetup.height))));

            this.put(ViewType.UI, new Tuple2<>(
                    new FloatRect(0f, 0f, 1f, 1f),
                    new View(new Vector2f(0, 0), new Vector2f(EngineSetup.width, EngineSetup.height))));

            this.put(ViewType.MINIMAP, new Tuple2<>(
                    new FloatRect(0.8f, 0.05f, 0.15f, 0.15f),
                    new View(new Vector2f(0, 0), new Vector2f(EngineSetup.width, EngineSetup.height)) {{
                        this.setViewport(new FloatRect(0.8f, 0.05f, 0.15f, 0.15f));
                    }}));
        }};

        // create a views map of (ViewType -> View), our one has the original viewport included
        var justViews = new HashMap<ViewType, View>();
        this.views.forEach((k, v) -> justViews.put(k, v.r));

        this.ecs = new ECS(List.of(new RenderSystem(this.mainWindow, justViews)), new BasicState());
        this.ecs.start();
    }

    public static void runForever() {
        var engine = new EngineSetup();
        engine.mainLoop();
    }

    private void enforceAspectRatio() {
        for (final var t : this.views.values()) {
            this.enforceViewAspectRatio(t.l, t.r);
        }
    }

    private void enforceViewAspectRatio(FloatRect initialView, View v) {
        var windowSize = this.mainWindow.getSize();

        var windowW = (float) windowSize.x;
        var windowH = (float) windowSize.y;

        var viewSize = v.getSize();
        var viewW = viewSize.x;
        var viewH = viewSize.y;

        var windowR = windowW / windowH;
        var viewR = viewW / viewH;

        // bars are vertical?
        boolean barPositionVertical = windowR < viewR;

        float finalW = initialView.width;
        float finalH = initialView.height;
        float finalOffsetX = initialView.left;
        float finalOffsetY = initialView.top;

        if (barPositionVertical) {
            var ratio = windowR / viewR;
            finalW *= ratio;
            finalOffsetY = (ratio * finalOffsetY) + (1f - ratio) / 2f;
        } else {
            var ratio = viewR / windowR;
            finalH *= ratio;
            finalOffsetX = (ratio * finalOffsetX) + (1f - ratio) / 2f;
        }

        v.setViewport(new FloatRect(finalOffsetX, finalOffsetY, finalH, finalW));
    }

    /**
     * Runs the game loop from the createWindow method
     * Takes events from the MainWindow and translates them to
     * proprietary StateEvent for the EventQueue
     */
    private void mainLoop() {
        int lastMouseX;
        int lastMouseY;

        {
            var initialMousePos = Mouse.getPosition(this.mainWindow);
            lastMouseX = initialMousePos.x;
            lastMouseY = initialMousePos.y;
        }

        while (this.mainWindow.isOpen()) {
            this.tilesInWindow();
            this.mainWindow.clear(Color.BLACK);
            for (final Event event : this.mainWindow.pollEvents()) {
                StateEvent se = new StateEvent() {
                };
                switch (event.type) {
                    case RESIZED: {
                        this.enforceAspectRatio();
                        break;
                    }
                    case KEY_PRESSED: {
                        KeyEvent keyEvent = event.asKeyEvent();
                        se = new scc210game.state.event.KeyPressedEvent(keyEvent.key);
                        break;
                    }
                    case KEY_RELEASED: {
                        KeyEvent keyEvent = event.asKeyEvent();
                        se = new scc210game.state.event.KeyDepressedEvent(keyEvent.key);
                        break;
                    }
                    case MOUSE_BUTTON_PRESSED: {
                        MouseButtonEvent msBtnEvent = event.asMouseButtonEvent();
                        se = new scc210game.state.event.MouseButtonPressedEvent(msBtnEvent.position.x, msBtnEvent.position.y, msBtnEvent.button);
                        break;
                    }
                    case MOUSE_BUTTON_RELEASED: {
                        MouseButtonEvent msBtnEvent = event.asMouseButtonEvent();
                        se = new scc210game.state.event.MouseButtonDepressedEvent(msBtnEvent.position.x, msBtnEvent.position.y, msBtnEvent.button);
                        break;
                    }
                    case MOUSE_MOVED: {
                        MouseEvent msMoved = event.asMouseEvent();
                        se = new scc210game.state.event.MouseMovedEvent(msMoved.position.x, msMoved.position.y,
                                msMoved.position.x - lastMouseX,
                                msMoved.position.y - lastMouseY);
                        lastMouseX = msMoved.position.x;
                        lastMouseY = msMoved.position.y;
                        break;
                    }
                    case CLOSED: {
                        this.mainWindow.close();
                        break;
                    }
                }
                this.ecs.runWithUpdateOnce(se);
            }
            this.ecs.runOnce();
            this.mainWindow.display();
        }
    }

    public void tilesInWindow(/*Tile[][] tiles*/) {
        double tileSize = 64;
        Vector2i windowSize = this.mainWindow.getSize();
        int tilesX = (int) Math.ceil(windowSize.x / tileSize);
        int tilesY = (int) Math.ceil(windowSize.y / tileSize);
        System.out.println("Window Width: " + windowSize.x + ". Tiles in width: " + tilesX);
        System.out.println("Window height: " + windowSize.y + ". Tiles in height: " + tilesY);

        // Need players coords. Then render tiles around player
    }
}
