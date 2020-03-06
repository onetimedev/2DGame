package scc210game.engine.render;

import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Resource;

public class MainViewResource extends Resource {

	public View mainView;
	public final Vector2f originalWindowSize;

	public MainViewResource(View mv) {
		this.mainView = mv;
		this.originalWindowSize = mv.getSize();
	}

	public void zoomIn() {
		this.mainView.setSize(originalWindowSize);
		this.mainView.zoom(0.6f);
	}

	public void zoomOut() {
		this.mainView.setSize(originalWindowSize);
	}

	@Override
	public boolean shouldKeep() {
	    return false;
	}
}
