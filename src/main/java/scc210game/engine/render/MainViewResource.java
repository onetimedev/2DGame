package scc210game.engine.render;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import org.jsfml.graphics.View;
import scc210game.engine.ecs.Resource;

public class MainViewResource extends Resource {

	public View mainView;

	public MainViewResource(View mv) {
		this.mainView = mv;
	}

	@Override
	public boolean shouldKeep() {
	    return false;
	}
}
