package scc210game.map;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import scc210game.ecs.Spawner;
import scc210game.ecs.World;
import scc210game.render.Render;
import scc210game.render.Renderable;

public class MapSpawner implements Spawner {

	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(new Map())
				.with(new Renderable((e,w) -> {
					// TODO: Go through all tiles, get texture, translate to location it should be, then w.draw(texture)
					// TODO: for each tile
					w.draw(e);
				}));
	}


}
