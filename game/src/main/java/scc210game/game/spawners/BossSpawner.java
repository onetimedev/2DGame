package scc210game.game.spawners;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;
import scc210game.engine.animation.Animate;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.utils.ResourceLoader;
import scc210game.game.components.TextureStorage;
import scc210game.game.map.Boss;
import scc210game.game.map.Enemy;
import scc210game.game.map.Map;

import java.time.Duration;
import java.util.Set;


/**
 *  Class to create a boss entity with its components
 */
public class BossSpawner implements Spawner {
    private String bossTexturePath;
    private final int bossNum;
    private final Vector2i[] bossCoords;
    private Texture t;
    private int id;
    private int damage;


    /**
     * Constructor to assign the properties used in the entities components
     * @param bc the coordinates of the boss (2x2)
     * @param bn  Grass = 0, Water = 1, Fire = 2, Ice = 3
     * @param map the map component
     * @param dmg the amount of damage the boss will do
     * @param id the enemy number
     */
    public BossSpawner(Vector2i[] bc, int bn, Map map, int dmg, int id) {
        this.bossCoords = bc;
        this.bossNum = bn;
        this.damage = dmg;
        this.id = id;

        for (final Vector2i v : this.bossCoords) {
            map.getTile(v.x, v.y).setHasEnemy(true);
            map.getTile(v.x, v.y).setHasCollision(true);
        }

        switch (this.bossNum) {
            case 0: {
                this.bossTexturePath = "textures/map/boss_grass.png";
                break;
            }
            case 1: {
                this.bossTexturePath = "textures/map/boss_water.png";
                break;
            }
            case 2: {
                this.bossTexturePath = "textures/map/boss_fire.png";
				        break;
            }
			      case 3: {
                this.bossTexturePath = "textures/map/boss_snow.png";
				        break;
			      }
		    }


        try {
            this.t = new Texture();
            this.t.loadFromStream(ResourceLoader.resolve(this.bossTexturePath));
        }
        catch (final Exception e) {
            throw new RuntimeException();
        }

	}


    /**
     * Constructor to assign attributes that will be used in the creation of the entity
     * @param builder the {@link World.EntityBuilder} to inject into
     * @param world the World the entity is being built in
     * @return the boss entity
     */
    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        return builder
                .with(new Enemy(false, this.damage, id))
                .with(new Boss())
                .with(new FilledInventorySpawner())
                .with(new Position(this.bossCoords[0].x, this.bossCoords[0].y))
                .with(new TextureStorage(this.bossTexturePath))
                .with(new Animate(Duration.ofMillis((600 * this.t.getSize().x) / 64 - 1), ((e, w) -> {
                }), true))
                .with(new Renderable(Set.of(ViewType.MAIN), 5, BossSpawner::accept));
    }


    /**
     * Method called during the rendering of the boss
     * @param entity the boss entity
     * @param window the main game window
     * @param world the world for this state
     */
    private static void accept(Entity entity, RenderWindow window, World world) {
        var enemy = world.fetchComponent(entity, Enemy.class);
        if(!enemy.defeated) {

                var p = world.fetchComponent(entity, Position.class);
            var textureStorage = world.fetchComponent(entity, TextureStorage.class);
            var animation = world.fetchComponent(entity, Animate.class);

            Sprite en = new Sprite(textureStorage.getTexture());
            en.setPosition(p.xPos * 64, p.yPos * 64);

            var numFrames = (textureStorage.getTexture().getSize().x / 128);

            var frame = (int) Math.floor(animation.pctComplete * (float) numFrames);

            int frameRow = frame / 8;
            int frameCol = frame % 8;
            en.setTextureRect(new IntRect(frameCol * 128, frameRow * 64, 128, 128));

            window.draw(en);

        }
    }
}
