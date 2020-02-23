package scc210game.engine.render;

import org.jsfml.graphics.RenderWindow;
import scc210game.engine.ecs.Resource;

public class MainWindowResource extends Resource {

	public RenderWindow mainWindow;

	public MainWindowResource(RenderWindow rw) {
        this.mainWindow = rw;
	}
}
