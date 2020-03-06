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
import scc210game.engine.ecs.System;
import scc210game.engine.movement.CombatMovement;
import scc210game.engine.movement.Movement;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.render.MainWindowResource;
import scc210game.engine.render.RenderSystem;
import scc210game.engine.render.ViewType;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.ui.systems.HandleClicked;
import scc210game.engine.ui.systems.HandleDragDrop;
import scc210game.engine.ui.systems.HandleHovered;
import scc210game.engine.ui.systems.HandleInteraction;
import scc210game.game.resources.ItemIDCounterResource;
import scc210game.game.resources.SavesDatabaseResource;
import scc210game.game.resources.ZoomStateResource;
import scc210game.game.states.MainMenuState;
import scc210game.game.systems.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * Singleton class to hold game loop, take entities to render, and change mainWindow views
 */
public class Main {
    public final Vector2f windowSize = new Vector2f(1920, 1080);
    public final RenderWindow mainWindow;
    public final Map<ViewType, View> views;
    private final ECS ecs;

    private Main() {
        this.mainWindow = new RenderWindow();
        this.mainWindow.create(new VideoMode(1920, 1080), "Elemental Guardian - A Return Home");
        this.mainWindow.setVerticalSyncEnabled(true);
        this.mainWindow.setFramerateLimit(60);
        this.views = new HashMap<>() {{
            this.put(ViewType.MAIN, new View(new Vector2f(0, 0), windowSize ){{
                this.zoom(0.7f);
            }});
            this.put(ViewType.UI, new View(new Vector2f(0, 0),  windowSize ));
            this.put(ViewType.MINIMAP, new View(new Vector2f(0, 0), new Vector2f(100, 80)));
        }};
        final List<Function<ECS, ? extends System>> systems = List.of(
                HandleInteraction::new,
                HandleHovered::new,
                HandleDragDrop::new,
                HandleClicked::new,
                (ecs) -> new AnimationUpdater(),
                Movement::new,
                CombatMovement::new,
                (ecs) -> new PositionUpdateSystem(),
                DialogueHandlingSystem::new,
                ItemMoveHandler::new,
                ToolTipHandler::new,
                InventoryLeaveHandler::new,
                CombatLeaveHandler::new,
                (ecs) -> new WaterShaderUpdaterSystem(),
                (ecs) -> new RenderSystem(this.mainWindow, this.views) // NOTE: always render last
        );
        this.ecs = new ECS(systems, new MainMenuState());
        this.ecs.addGlobalResource(new MainViewResource(this.views.get(ViewType.MAIN)));
        this.ecs.addGlobalResource(new ZoomStateResource(false));
        this.ecs.addGlobalResource(new MainWindowResource(this.mainWindow));
        this.ecs.addGlobalResource(new ItemIDCounterResource());
        this.ecs.addGlobalResource(new SavesDatabaseResource());
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
                        this.ecs.getCurrentWorld().deactivateCombat();
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
