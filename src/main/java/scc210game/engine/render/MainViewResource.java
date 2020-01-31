package scc210game.engine.render;

import org.jsfml.graphics.View;
import scc210game.engine.ecs.Resource;

public class MainViewResource extends Resource {

	public View mainView;

	public MainViewResource(View mv) {
		mainView = mv;
	}

	@Override
	public String serialize() {
		return null;
	}
}
