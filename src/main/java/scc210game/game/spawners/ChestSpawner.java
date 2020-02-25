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
	private Texture t = new Texture();

	public ChestSpawner(Tile ti) {
		chestTile = ti;
		if(chestTile.getYPos() < 60 && chestTile.getXPos() < 60)
			chestTile.setTexture("sand.png");
		else if(chestTile.getYPos() < 60 && chestTile.getXPos() > 60)
			chestTile.setTexture("light_basalt.png");
		else if(chestTile.getYPos() > 60 && chestTile.getXPos() < 55)
			chestTile.setTexture("grass.png");
		else if(chestTile.getYPos() > 60 && chestTile.getXPos() > 40)
			chestTile.setTexture("snow.png");
		if((chestTile.getYPos() == 49 && chestTile.getXPos() == 112) || (chestTile.getYPos() == 61 && chestTile.getXPos() == 113))
			chestTile.setTexture("grass.png");

		t = MapHelper.loadTexture("chest.png");
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
