package scc210game.render;

import org.jsfml.graphics.View;
import scc210game.ecs.Resource;

public class MainViewResource extends Resource {

	public View mainView;

	public MainViewResource(View mv) {
		System.out.println(mv);
		mainView = mv;
	}

	@Override
	public String serialize() {
		return null;
	}
}
