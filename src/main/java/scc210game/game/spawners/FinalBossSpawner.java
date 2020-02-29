package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.map.Enemy;
import scc210game.game.map.FinalBoss;
import scc210game.game.components.TextureStorage;

import java.util.Set;

public class FinalBossSpawner implements Spawner {
    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        return builder
                .with(new Enemy(false))
                .with(new FinalBoss())
                .with(new Position(59, 59))
                .with(new TextureStorage("textures/boss_final.png"))
                .with(new Renderable(Set.of(ViewType.MAIN), 5, FinalBossSpawner::accept));
    }

    private static void accept(Entity entity, RenderWindow window, World world) {
        var p = world.fetchComponent(entity, Position.class);
        var t = world.fetchComponent(entity, TextureStorage.class);
        Sprite en = new Sprite(t.getTexture());
        en.setPosition(p.xPos * 64, p.yPos * 64);
        window.draw(en);
    }
}
