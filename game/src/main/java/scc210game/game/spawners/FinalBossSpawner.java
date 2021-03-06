package scc210game.game.spawners;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import scc210game.engine.animation.Animate;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.map.Enemy;
import scc210game.game.map.FinalBoss;
import scc210game.game.components.TextureStorage;

import java.time.Duration;
import java.util.Set;

/**
 * Class to create a final boss entity with its components
 */
public class FinalBossSpawner implements Spawner {

    private int damage;
    private int id;

    /**
     * Constructor to assign the boss' damage amount and id number
     * @param dmg damage the boss will do
     * @param id enemy number
     */
    public FinalBossSpawner(int dmg, int id) {
        damage = dmg;
        this.id = id;
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
                .with(new Enemy(false, this.damage, this.id))
                .with(new FinalBoss())
                .with(new FilledInventorySpawner())
                .with(new Position(59, 59))
                .with(new TextureStorage("textures/map/boss_final.png"))
                .with(new Animate(Duration.ofMillis((900 * 192) / 64 - 1), ((e, w) -> {
                }), true))
                .with(new Renderable(Set.of(ViewType.MAIN), 5, FinalBossSpawner::accept));
    }


    /**
     * Method called during the rendering of the final boss
     * @param entity the final boss entity
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

            var numFrames = (textureStorage.getTexture().getSize().x / 192);

            var frame = (int) Math.floor(animation.pctComplete * (float) numFrames);

            int frameRow = frame / 8;
            int frameCol = frame % 8;
            en.setTextureRect(new IntRect(frameCol * 192, frameRow * 64, 192, 192));

            window.draw(en);
        }
    }
}
