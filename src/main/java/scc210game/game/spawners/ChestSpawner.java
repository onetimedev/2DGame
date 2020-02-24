package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.map.Chest;
import scc210game.game.map.TextureStorage;
import scc210game.game.map.Tile;

import java.util.Set;

public class ChestSpawner implements Spawner {
    private final Tile chestTile;

    public ChestSpawner(Tile ti) {
        this.chestTile = ti;
        if (this.chestTile.getYPos() < 60 && this.chestTile.getXPos() < 60)
            this.chestTile.setTexture("sand.png");
        else if (this.chestTile.getYPos() < 60 && this.chestTile.getXPos() > 60)
            this.chestTile.setTexture("light_basalt.png");
        else if (this.chestTile.getYPos() > 60 && this.chestTile.getXPos() < 55)
            this.chestTile.setTexture("grass.png");
        else if (this.chestTile.getYPos() > 60 && this.chestTile.getXPos() > 40)
            this.chestTile.setTexture("snow.png");
        if ((this.chestTile.getYPos() == 49 && this.chestTile.getXPos() == 112) || (this.chestTile.getYPos() == 61 && this.chestTile.getXPos() == 113))
            this.chestTile.setTexture("grass.png");
    }

	@Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        return builder
                .with(new Chest(this.chestTile))
                .with(new Position(this.chestTile.getXPos(), this.chestTile.getYPos()))
                .with(new TextureStorage("textures/chest.png"))
                .with(new Renderable(Set.of(ViewType.MAIN), 5,
                        ChestSpawner::accept));

    }

    private static void accept(Entity entity, RenderWindow window, World world) {
        var t = world.fetchComponent(entity, TextureStorage.class);
        var c = world.fetchComponent(entity, Chest.class);

        Sprite s = new Sprite(t.getTexture());
        s.setPosition(c.tile.getXPos() * 64, c.tile.getYPos() * 64);
        window.draw(s);
    }
}
