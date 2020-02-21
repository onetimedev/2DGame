package scc210game.game;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;
import scc210game.engine.animation.AnimationUpdater;
import scc210game.engine.ecs.ECS;
import scc210game.engine.ecs.System;
import scc210game.engine.movement.Movement;
import scc210game.engine.render.*;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.ui.systems.HandleClicked;
import scc210game.engine.ui.systems.HandleDragDrop;
import scc210game.engine.ui.systems.HandleHovered;
import scc210game.engine.ui.systems.HandleInteraction;
import scc210game.engine.utils.Tuple2;
import scc210game.game.components.PositionUpdateSystem;
import scc210game.game.states.MainMenuState;
import scc210game.game.systems.DialogueHandlingSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * Singleton class to hold game loop, take entities to render, and change mainWindow views
 */
public class Main {
    private static final int width = 1920;
    private static final int height = 1080;

    private final RenderWindow mainWindow;
    private final Map<ViewType, Tuple2<FloatRect, View>> views;
    private final ECS ecs;

    private Main() {
        this.mainWindow = new RenderWindow();
        this.mainWindow.create(new VideoMode(width, height), "SCC210 Game");
        this.mainWindow.setVerticalSyncEnabled(true);
        this.mainWindow.setFramerateLimit(60);

        var viewOrder = List.of(ViewType.MAIN, ViewType.MINIMAP, ViewType.UI);

        this.views = new HashMap<>() {{
            this.put(ViewType.MAIN, new Tuple2<>(
                    new FloatRect(0f, 0f, 1f, 1f),
                    new View(new Vector2f(0, 0), new Vector2f(width, height))));

            this.put(ViewType.MINIMAP, new Tuple2<>(
                    new FloatRect(0.8f, 0.05f, 0.15f, 0.15f),
                    new View(new Vector2f(0, 0), new Vector2f(width, height)) {{
                        this.setViewport(new FloatRect(0.8f, 0.05f, 0.15f, 0.15f));
                        this.zoom(2f);
                    }}));

            this.put(ViewType.UI, new Tuple2<>(
                    new FloatRect(0f, 0f, 1f, 1f),
                    new View(new Vector2f(0, 0), new Vector2f(width, height))));

        }};

        // create a views map of (ViewType -> View), our one has the original viewport included
        var justViews = new HashMap<ViewType, View>();
        this.views.forEach((k, v) -> justViews.put(k, v.r));

        final List<Function<ECS, ? extends System>> systems = List.of(
                HandleInteraction::new,
                HandleHovered::new,
                HandleDragDrop::new,
                HandleClicked::new,
                (ecs) -> new AnimationUpdater(),
                Movement::new,
                (ecs) -> new PositionUpdateSystem(),
                DialogueHandlingSystem::new,
                (ecs) -> new RenderSystem(this.mainWindow, justViews, viewOrder) // NOTE: always render last
        );
        this.ecs = new ECS(systems, new MainMenuState());
        this.ecs.addGlobalResource(new MainViewResource(justViews.get(ViewType.MAIN)));
        this.ecs.addGlobalResource(new MinimapViewResource(justViews.get(ViewType.MINIMAP)));
        this.ecs.addGlobalResource(new MainWindowResource(this.mainWindow));
        this.ecs.start();
    }

    public static void runForever() {
        var engine = new Main();
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

        var screenSize = this.mainWindow.getSize();

        while (this.mainWindow.isOpen() && this.ecs.isRunning()) {
            this.mainWindow.clear(Color.BLACK);
            for (final var event : this.mainWindow.pollEvents()) {
                StateEvent se = new StateEvent() {
                };
                switch (event.type) {
                    case RESIZED: {
                        this.enforceAspectRatio();
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
