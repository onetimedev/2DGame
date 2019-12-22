package scc210game.render;

import org.jsfml.graphics.*;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;


public class Render {

	public static RenderWindow mainWindow;


	public Render(int w, int h) {
		gameWindow(w, h);
		startGameLoop();
	}



	public void gameWindow(int width, int height) {
		mainWindow = new RenderWindow();
		mainWindow.create(new VideoMode(width, height), "Explore");
	}


	public void startGameLoop() {
		while (mainWindow.isOpen()) {
			mainWindow.clear(Color.BLUE);
			//Display what was drawn (... the red color!)
			mainWindow.display();
			eventsCheck();

		}
	}


	public void eventsCheck() {
		for(Event event : mainWindow.pollEvents()) {
			if(event.type == Event.Type.CLOSED) {
					mainWindow.close();
				}
			}
	}






}
