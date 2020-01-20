package scc210game.render;

import org.jsfml.graphics.*;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;
import scc210game.ecs.ECS;
import scc210game.state.event.StateEvent;
import java.util.List;


/**
 * Singleton class to hold game loop, take entities to render, and change mainWindow views
 */
public class EngineSetup {
	private static EngineSetup instance = null;
	public RenderWindow mainWindow;
	private ECS ecs;
	private EngineSetup() {
		mainWindow = new RenderWindow();
		ecs = new ECS(List.of(new RenderSystem(mainWindow)), new BasicState());  // Add all systems / groups of systems here into the List
		ecs.start();
		createWindow(1920, 1080);
	}

	private View view;


	/**
	 * To create or get the instance of the singleton class
	 * @return the singleton instance of Render
	 */
	public static EngineSetup getInstance() {
		if(instance == null)
			instance = new EngineSetup();
		return instance;
	}


	/**
	 * Create a window given a size
	 * @param width of the window
	 * @param height of the window
	 */
	public void createWindow(int width, int height) {
		mainWindow.create(new VideoMode(width, height), "Explore");
		//view = new View(mainWindow.getDefaultView().getCenter(), mainWindow.getDefaultView().getSize());
		//mainWindow.setView(view);
		mainLoop();
	}


	/**
	 * Runs the game loop from the createWindow method
	 *  Takes events from the MainWindow and translates them to
	 *  proprietary StateEvent for the EventQueue
	 */
	private void mainLoop() {
		while(mainWindow.isOpen()) {
			mainWindow.clear(Color.BLACK);
			System.out.println("Window cleared");
			for(Event event: mainWindow.pollEvents()) {
				StateEvent se = new StateEvent(){};
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
					case CLOSED: {
						mainWindow.close();
						break;
					}
					case RESIZED: {
						//view.setSize(mainWindow.getSize().x, mainWindow.getSize().y);
						break;
					}
				}
			ecs.runWithUpdateOnce(se);
			}
		ecs.runOnce();
		mainWindow.display();
		}
	}





}
