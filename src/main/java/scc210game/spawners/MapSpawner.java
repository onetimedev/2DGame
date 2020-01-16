package scc210game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2i;
import scc210game.ecs.Entity;
import scc210game.ecs.Spawner;
import scc210game.ecs.World;
import scc210game.map.Map;
import scc210game.render.Renderable;

public class MapSpawner implements Spawner {

	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(new Map())
				.with(new Renderable((Entity entity, RenderWindow window, World world) -> {
					// TODO: Go through all tiles, get texture, translate to location it should be, then w.draw(texture)
					// TODO: for each tile

					Map m = world.fetchComponent(entity, Map.class);
					Vector2i testPlayerCoords = new Vector2i(0,0);


					// number of tiles that can fit in window
					int tilesInWindow = (window.getSize().x / 64) * (window.getSize().y / 64);
					// work out the top left most renderable tile from the player coords and then
					// 


					//Position player = world.fetchComponent(entity, Player.class);
					// Based on coords look at tiles around this that would fit in the window size


					// w.draw(tile);

				}, 0));  // Everything in map will have depth 0
	}


}
