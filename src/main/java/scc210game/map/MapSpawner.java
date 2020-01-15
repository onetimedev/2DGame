package scc210game.map;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import scc210game.ecs.Spawner;
import scc210game.ecs.World;
import scc210game.render.Render;
import scc210game.render.Renderable;

public class MapSpawner implements Spawner {
	private Map map;

	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(map = new Map())
				.with(new Renderable((e,w) -> {
					// TODO: Go through all tiles, get texture, translate to location it should be, then w.draw(texture) for each tile
					for (int x=0; x<map.getWidth() ; x++)
						for (int y=0; y<map.getHeight(); y++) {


							//w.draw(/*map.getTile(x, y)*/);
						}







				}));
	}


}
