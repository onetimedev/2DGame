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
import scc210game.game.map.Map;
import scc210game.game.utils.MapHelper;

import java.util.Set;

public class BossSpawner implements Spawner {
    private Texture bossTexture = new Texture();
    private final int bossNum;
    private final Vector2i[] bossCoords;

    /*
        Create the boss texture based on coordinates and boss number.
        Grass = 0, Water = 1, Fire = 2, Ice = 3
    */
    public BossSpawner(Vector2i[] bc, int bn, Map map) {
        this.bossCoords = bc;
        this.bossNum = bn;

        for (final Vector2i v : this.bossCoords) {
            map.getTile(v.x, v.y).setHasEnemy(true);
        }

        switch (this.bossNum) {
            case 0: {
                this.bossTexture = MapHelper.loadTexture("boss_grass.png");
                break;
            }
            case 1: {
                this.bossTexture = MapHelper.loadTexture("boss_water.png");
                break;
            }
            case 2: {
                this.bossTexture = MapHelper.loadTexture("boss_fire.png");
				break;
			}
			case 3: {
                this.bossTexture = MapHelper.loadTexture("boss_snow.png");
				break;
			}
		}
	}




	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder) {
		return builder
                .with(new Enemy(false))
                .with(new Boss())
                .with(new Position(this.bossCoords[0].x, this.bossCoords[0].y))
				.with(new Renderable(Set.of(ViewType.MAIN), 5,
						(Entity entity, RenderWindow window, World world) -> {

                            //TODO: Get if specific enemy has been defeated
                            //if(defeated == false) {
                            Sprite en = new Sprite(this.bossTexture);
                            en.setPosition(this.bossCoords[0].x * 64, this.bossCoords[0].y * 64);
                            window.draw(en);
                            //}

                        }));
	}

}
