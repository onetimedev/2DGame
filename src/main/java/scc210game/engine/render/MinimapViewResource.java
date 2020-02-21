package scc210game.engine.render;

import org.jsfml.graphics.View;
import scc210game.engine.ecs.Resource;

public class MinimapViewResource extends Resource {

	public View minimapView;

	public MinimapViewResource(View mv) {
		this.minimapView = mv;
	}

	@Override
	public String serialize() {
		return null;
	}
}
