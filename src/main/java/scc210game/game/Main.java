package scc210game.game;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

import scc210game.engine.animation.AnimationUpdater;
import scc210game.engine.ecs.ECS;
import scc210game.engine.movement.Movement;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.render.RenderSystem;
import scc210game.engine.render.ViewType;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.ui.systems.HandleClicked;
import scc210game.engine.ui.systems.HandleDragDrop;
import scc210game.engine.ui.systems.HandleHovered;
import scc210game.engine.ui.systems.HandleInteraction;
import scc210game.game.states.MainMenuState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Singleton class to hold game loop, take entities to render, and change mainWindow views
 */
public class Main {
    public final RenderWindow mainWindow;
    public final Map<ViewType, View> views;
    private final ECS ecs;

    private Main() {
        this.mainWindow = new RenderWindow();
        this.mainWindow.create(new VideoMode(1920, 1080), "SCC210 Game");
        this.mainWindow.setFramerateLimit(60);
        this.views = new HashMap<>() {{
            this.put(ViewType.MAIN, new View(new Vector2f(0, 0), new Vector2f(Main.this.mainWindow.getSize()) ){{
                //this.zoom(0.f);
            }});
            this.put(ViewType.MINIMAP, new View(new Vector2f(0, 0), new Vector2f(100, 80)));
        }};
        final var systems = List.of(
                new HandleInteraction(),
                new HandleHovered(),
                new HandleDragDrop(),
                new HandleClicked(),
                new AnimationUpdater(),
                new Movement(),
                new RenderSystem(this.mainWindow, this.views) // NOTE: always render last
        );
        this.ecs = new ECS(systems, new MainMenuState());
        this.ecs.addGlobalResource(new MainViewResource(this.views.get(ViewType.MAIN)));
        this.ecs.start();
    }

    public static void runForever() {
        var engine = new Main();
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

        var screenSize = this.mainWindow.getSize();

        while (this.mainWindow.isOpen() && this.ecs.isRunning()) {
            this.mainWindow.clear(Color.BLACK);
            for (final Event event : this.mainWindow.pollEvents()) {
                StateEvent se = new StateEvent() {
                };
                switch (event.type) {
                    case RESIZED: {
                        screenSize = this.mainWindow.getSize();
                        break;
                    }
                    case KEY_PRESSED: {
                        KeyEvent keyEvent = event.asKeyEvent();
                        se = new scc210game.engine.state.event.KeyPressedEvent(keyEvent.key);
                        break;
                    }
                    case KEY_RELEASED: {
                        KeyEvent keyEvent = event.asKeyEvent();
                        se = new scc210game.engine.state.event.KeyDepressedEvent(keyEvent.key);
                        break;
                    }
                    case MOUSE_BUTTON_PRESSED: {
                        MouseButtonEvent msBtnEvent = event.asMouseButtonEvent();
                        se = new scc210game.engine.state.event.MouseButtonPressedEvent(
                                msBtnEvent.position.x / (float) screenSize.x,
                                msBtnEvent.position.y / (float) screenSize.y, msBtnEvent.button);
                        break;
                    }
                    case MOUSE_BUTTON_RELEASED: {
                        MouseButtonEvent msBtnEvent = event.asMouseButtonEvent();
                        se = new scc210game.engine.state.event.MouseButtonDepressedEvent(
                                msBtnEvent.position.x / (float) screenSize.x,
                                msBtnEvent.position.y / (float) screenSize.y, msBtnEvent.button);
                        break;
                    }
                    case MOUSE_MOVED: {
                        MouseEvent msMoved = event.asMouseEvent();
                        se = new scc210game.engine.state.event.MouseMovedEvent(
                                msMoved.position.x / (float) screenSize.x,
                                msMoved.position.y / (float) screenSize.y,
                                (msMoved.position.x - lastMouseX) / (float) screenSize.x,
                                (msMoved.position.y - lastMouseY) / (float) screenSize.y);
                        lastMouseX = msMoved.position.x;
                        lastMouseY = msMoved.position.y;
                        break;
                    }
                    case CLOSED: {
                        this.mainWindow.close();
                        break;
                    }
                }
                this.ecs.acceptEvent(se);
            }
            this.ecs.runOnce();
            this.mainWindow.display();
        }
    }

}
