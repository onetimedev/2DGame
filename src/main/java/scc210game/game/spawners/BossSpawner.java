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
import scc210game.game.map.Boss;
import scc210game.game.map.Enemy;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class BossSpawner implements Spawner {
	private Texture bossTexture = new Texture();
	private String assetsPath = "./src/main/resources/textures/";
	private int bossNum;
	private Vector2i[] bossCoords;

	/*
	Grass = 0, Water = 1, Fire = 2, Ice = 3
	*/
	public BossSpawner(Vector2i[] bc, int bn) {
		bossCoords = bc;
		bossNum = bn;
		switch(bossNum) {
			case 0: {
				loadTexture("boss_grass.png");
				break;
			}
			case 1: {
				loadTexture("boss_water.png");
				break;
			}
			case 2: {
				loadTexture("boss_fire.png");
				break;
			}
			case 3: {
				loadTexture("boss_snow.png");
				break;
			}
		}
	}


	public void loadTexture(String fileName) {
		try {
			bossTexture.loadFromFile(Paths.get(assetsPath, fileName));
		}
		catch(IOException e) {
			throw new RuntimeException(e);
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
