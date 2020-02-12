package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.utils.MapHelper;
import scc210game.game.map.Boss;
import scc210game.game.map.Enemy;
import scc210game.game.map.Map;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class BossSpawner implements Spawner {
	private Texture bossTexture = new Texture();
	private int bossNum;
	private Vector2i[] bossCoords;

	/*
		Create the boss texture based on coordinates and boss number.
		Grass = 0, Water = 1, Fire = 2, Ice = 3
	*/
	public BossSpawner(Vector2i[] bc, int bn, Map map) {
		bossCoords = bc;
		bossNum = bn;

		for (Vector2i v: bossCoords) {
			map.getTile(v.x, v.y).setHasEnemy(true);
		}

		switch(bossNum) {
			case 0: {
				bossTexture = MapHelper.loadTexture("boss_grass.png");
				break;
			}
			case 1: {
				bossTexture = MapHelper.loadTexture("boss_water.png");
				break;
			}
			case 2: {
				bossTexture = MapHelper.loadTexture("boss_fire.png");
				break;
			}
			case 3: {
				bossTexture = MapHelper.loadTexture("boss_snow.png");
				break;
			}
		}
	}




	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
				.with(new Enemy())
				.with(new Boss())
				.with(new Position(bossCoords[0].x, bossCoords[0].y))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
						(Entity entity, RenderWindow window, World world) -> {

							Sprite en = new Sprite(bossTexture);
							en.setPosition(bossCoords[0].x *64, bossCoords[0].y *64);
							window.draw(en);

						}));
	}

}
