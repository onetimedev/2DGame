package scc210game.render;

import org.jsfml.graphics.*;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;
import scc210game.ecs.ECS;
import scc210game.ecs.Entity;
import scc210game.ecs.Query;
import scc210game.ecs.World;
import scc210game.events.EventQueue;
import scc210game.state.event.StateEvent;

import java.util.List;
import java.util.stream.Stream;

import static org.jsfml.window.event.Event.Type.KEY_PRESSED;
import static org.jsfml.window.event.Event.Type.KEY_RELEASED;


/**
 * Singleton class to hold game loop, take entities to render, and change mainWindow views
 */
public class Render {

	private static Render instance = null;
	public static RenderWindow mainWindow;
	// Add all systems / groups of systems here into the List
	private static ECS ecs = new ECS(List.of(new RenderSystem()), new BasicState());

	public Render() {
		mainWindow = new RenderWindow();
	}

	
	/**
	 * To create or get the instance of the singleton class
	 * @return the singleton instance of Render
	 */
	public static Render getInstance() {
		if (instance == null)
			instance = new Render();
		return instance;
	}


	/**
	 * Create a window given a size
	 * @param width of the window
	 * @param height of the window
	 */
	public static void createWindow(int width, int height) {
		mainWindow.create(new VideoMode(width, height), "Explore");
		Render.mainLoop();
	}


	/**
	 * Runs the game loop from the createWindow method
	 *  Takes events from the MainWindow and translates them to
	 *  proprietary StateEvent for the EventQueue
	 */
	private static void mainLoop() {
		while(mainWindow.isOpen()) {
			mainWindow.clear(Color.BLACK);
			for(Event event: mainWindow.pollEvents()) {
				StateEvent se;
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
						se = new scc210game.state.event.MouseMovedEvent(msMoved.position.x, msMoved.position.y);
						break;
					}

				}
				ecs.runWithUpdateOnce(se);
		}
			mainWindow.display();
		}
	}


	/**
	 * Method to set a view in the mainWindow
	 * @param camView the camera view to be added
	 */
	public static void setView(Camera camView) {
		mainWindow.setView(camView);  // Make the window use the alternate view
	}




}
