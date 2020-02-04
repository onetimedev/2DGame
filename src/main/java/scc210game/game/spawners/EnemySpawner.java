package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
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
  private int xSpawn;
  private int ySpawn;

    public EnemySpawner(Tile t) {
      enemyTile = t;
      setType(enemyTile.getTextureName());
      xSpawn = enemyTile.getXPos();
      ySpawn = enemyTile.getYPos();
    }

    public void setType(String textureName) {
      switch (textureName) {
        case "enemy_basalt.png": {
          enemyTile.setTexture("enemy.png");  //TODO: change to correct biomeEnemy.png
          enemyTile.setHasCollision(true);
          break;
        }
        case "enemy_sand.png": {
          enemyTile.setTexture("enemy.png");  //TODO: change to correct biomeEnemy.png
          enemyTile.setHasCollision(true);
          break;
        }
        case "enemy_grass.png": {
          enemyTile.setTexture("enemy.png");   //TODO: change to correct biomeEnemy.png
          enemyTile.setHasCollision(true);
          break;
        }
        case "enemy_snow": {
          enemyTile.setTexture("enemy.png");   //TODO: change to correct biomeEnemy.png
          enemyTile.setHasCollision(true);
          break;
        }
      }



    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        return builder
                .with(new Enemy())
                .with(new Position(xSpawn, ySpawn))
                .with(new Renderable(Set.of(ViewType.MAIN), 5,
                        (Entity entity, RenderWindow window, World world) -> {

                          Sprite en = new Sprite(enemyTile.getTexture());
                          en.setPosition(xSpawn*64, ySpawn*64);

                          window.draw(en);


                        }));

    }
}