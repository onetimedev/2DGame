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

public class FinalBossSpawner implements Spawner {

    private int damage;
    private int id;
    public FinalBossSpawner(int dmg, int id) {
        damage = dmg;
        this.id = id;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        return builder
                .with(new Enemy(false, this.damage, this.id))
                .with(new FinalBoss())
                .with(new Position(59, 59))
                .with(new TextureStorage("textures/map/boss_final.png"))
                .with(new Animate(Duration.ofMillis((900 * 192) / 64 - 1), ((e, w) -> {
                }), true))
                .with(new Renderable(Set.of(ViewType.MAIN), 5, FinalBossSpawner::accept));
    }

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
