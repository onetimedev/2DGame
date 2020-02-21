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
import scc210game.game.map.Chest;
import scc210game.game.map.Tile;
import scc210game.game.utils.MapHelper;

import java.util.Set;

public class ChestSpawner implements Spawner {

    private final Tile chestTile;
    private Texture t = new Texture();

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

        this.t = MapHelper.loadTexture("chest.png");
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        return builder
                .with(new Chest())
                .with(new Position(this.chestTile.getXPos(), this.chestTile.getYPos()))
                .with(new Renderable(Set.of(ViewType.MAIN, ViewType.MINIMAP), 5,
                        (Entity entity, RenderWindow window, World world) -> {

                            Sprite c = new Sprite(this.t);
                            c.setPosition(this.chestTile.getXPos() * 64, this.chestTile.getYPos() * 64);
                            window.draw(c);

                        }));

    }
}
