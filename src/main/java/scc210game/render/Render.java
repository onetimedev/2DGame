package scc210game.render;

import org.jsfml.graphics.*;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;
import org.jsfml.window.event.KeyEvent;
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
	 */
	private static void mainLoop() {
		while(mainWindow.isOpen()) {
			mainWindow.clear(Color.BLACK);
			for(Event event: mainWindow.pollEvents()) {
				StateEvent se;
				switch (event.type) {
					case KEY_PRESSED: {
						KeyEvent kEvent = event.asKeyEvent();
						scc210game.state.event.KeyPressedEvent ksEvent = new scc210game.state.event.KeyPressedEvent(kEvent.key);
						se = ksEvent;
						break;
					}
					case KEY_RELEASED: {

					}
					// TODO: Add state.event all events to the switch
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
