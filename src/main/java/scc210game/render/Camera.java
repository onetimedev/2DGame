package scc210game.render;


import org.jsfml.graphics.View;


/**
 * Represents the view of a region based on its zoom and size
 */
public class Camera extends View {

	public View defaultView = (View) Render.mainWindow.getDefaultView();

	public Camera(int zoom, int width, int height) {
		View cameraView = new View(defaultView.getCenter(), defaultView.getSize());
	}







}
