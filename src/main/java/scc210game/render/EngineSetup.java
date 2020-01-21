package scc210game.render;

import org.jsfml.graphics.Color;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Singleton class to hold game loop, take entities to render, and change mainWindow views
 */
public class EngineSetup {
    public final RenderWindow mainWindow;
    public final Map<ViewType, View> views;
    private final ECS ecs;

    private EngineSetup() {
        this.mainWindow = new RenderWindow();
        this.mainWindow.create(new VideoMode(720, 480), "SCC210 Game");
        this.mainWindow.setFramerateLimit(60);
        this.views = new HashMap<>() {{
            this.put(ViewType.MAIN, new View(new Vector2f(0, 0), new Vector2f(mainWindow.getSize())));
            this.put(ViewType.MINIMAP, new View(new Vector2f(0, 0), new Vector2f(100, 80)));
        }};
        this.ecs = new ECS(List.of(new RenderSystem(this.mainWindow, this.views)), new BasicState());
        this.ecs.start();
    }

    public static void runForever() {
        var engine = new EngineSetup();
        engine.mainLoop();
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
