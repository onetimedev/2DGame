package scc210game.render;

import org.jsfml.graphics.*;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;


/**
 * Singleton class to hold game loop, take entities to render, and change mainWindow views
 */
public class Render {

	private static Render instance = null;
	public static RenderWindow mainWindow;


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
		Render.startGameLoop();
	}


	/**
	 * Runs the game loop from the createWindow method
	 */
	private static void startGameLoop() {
		while (mainWindow.isOpen()) {
			mainWindow.clear(Color.BLACK);
			mainWindow.display();
			Render.eventsCheck();
		}
	}


	/**
	 * Method to set a view in the mainWindow
	 * @param camView the camera view to be added
	 */
	public static void setView(Camera camView) {
		mainWindow.setView(camView);  // Make the window use the alternate view
	}


	/**
	 * Method to draw a shape in the window
	 * @param shapeToDisplay
	 */
	public static void displayInWindow(Shape shapeToDisplay) {
		mainWindow.draw(shapeToDisplay);

	}


	/**
	 *  needs to translate from JSFML events to event queue events
	*/
	public static void eventsCheck() {
		for(Event event : mainWindow.pollEvents()) {
			if(event.type == Event.Type.CLOSED) {
					mainWindow.close();
				}
			}
	}


}
