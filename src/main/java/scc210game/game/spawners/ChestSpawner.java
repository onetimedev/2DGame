package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.utils.MapHelper;
import scc210game.game.map.Chest;
import scc210game.game.map.Tile;

import java.util.Set;

public class ChestSpawner implements Spawner {

	private Tile chestTile;
	private Texture t;

	public ChestSpawner(Tile ti) {
		chestTile = ti;
		t = MapHelper.loadTexture("chest.png");
		MapHelper.setTileToBiome(chestTile);
	}

	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(new Chest())
				.with(new Position(chestTile.getXPos(), chestTile.getYPos()))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
						(Entity entity, RenderWindow window, World world) -> {

								Sprite c = new Sprite(t);
								c.setPosition(chestTile.getXPos()*64, chestTile.getYPos()*64);
								window.draw(c);

						}));

	}
}
