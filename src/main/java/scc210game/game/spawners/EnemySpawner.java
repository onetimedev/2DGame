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
import scc210game.engine.utils.MapHelper;
import scc210game.game.map.Enemy;
import scc210game.game.map.Tile;

import java.util.Set;

public class EnemySpawner implements Spawner {
  private Tile enemyTile;
  private int xSpawn;
  private int ySpawn;
  private Texture enemyTexture = new Texture();


  public EnemySpawner(Tile t) {
    enemyTile = t;
    xSpawn = enemyTile.getXPos();
    ySpawn = enemyTile.getYPos();
    setTexture(t.getTextureName());
  }


  public void setTexture(String type) {
    switch (type) {
      case "enemy_basalt.png": {
        enemyTile.setHasCollision(true);
        enemyTexture = MapHelper.loadTexture("fireEnemy.png");
        break;
      }
      case "enemy_sand.png": {
        enemyTile.setHasCollision(true);
        enemyTexture = MapHelper.loadTexture("waterEnemy.png");
        break;
      }
      case "enemy_grass.png": {
        enemyTile.setHasCollision(true);
        enemyTexture = MapHelper.loadTexture("grassEnemy.png");
        break;
      }
      case "enemy_snow": {
        enemyTile.setHasCollision(true);
        enemyTexture = MapHelper.loadTexture("enemy.png");
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

          Sprite en = new Sprite(enemyTexture);
          en.setPosition(xSpawn*64, ySpawn*64);

          window.draw(en);
        }));
  }
}