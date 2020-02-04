package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.map.Enemy;
import scc210game.game.map.Map;
import scc210game.game.map.Tile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class EnemySpawner implements Spawner {
  private Tile enemyTile;

    EnemySpawner(Tile t) {
      enemyTile = t;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        return builder
                .with(new Enemy())
                .with(new Position(15, 106))
                .with(new Renderable(Set.of(ViewType.MAIN), 5,
                        (Entity entity, RenderWindow window, World world) -> {
                            Texture t = new Texture();
                            try {
                                t.loadFromFile(Paths.get("./src/main/resources/textures/lava.png"));

                              var mapEnt = world.applyQuery(Query.builder().require(Map.class).build()).findFirst().get();
                              var map = world.fetchComponent(mapEnt, Map.class);


                              Sprite en = new Sprite(t);
                              en.setPosition(0, 0);

                              window.draw(en);

                            }
                            catch (IOException e) {
                                throw new RuntimeException();
                            }



                        }));

    }
}