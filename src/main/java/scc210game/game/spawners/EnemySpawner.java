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
import scc210game.game.map.Enemy;
import scc210game.game.map.Tile;
import scc210game.game.utils.MapHelper;

import java.util.Set;

public class EnemySpawner implements Spawner {
    private final Tile enemyTile;
    private final int xSpawn;
    private final int ySpawn;
    private Texture enemyTexture = new Texture();


    public EnemySpawner(Tile t) {
        this.enemyTile = t;
        this.xSpawn = this.enemyTile.getXPos();
        this.ySpawn = this.enemyTile.getYPos();
        this.setTexture(t.getTextureName());
        this.enemyTile.setHasEnemy(true);
    }


  public void setTexture(String type) {
    switch (type) {
      case "enemy_basalt.png": {
          this.enemyTile.setHasCollision(true);
          this.enemyTexture = MapHelper.loadTexture("fireEnemy.png");
          break;
      }
      case "enemy_sand.png": {
          this.enemyTile.setHasCollision(true);
          this.enemyTexture = MapHelper.loadTexture("waterEnemy.png");
          break;
      }
      case "enemy_grass.png": {
          this.enemyTile.setHasCollision(true);
          this.enemyTexture = MapHelper.loadTexture("grassEnemy.png");
          break;
      }
      case "enemy_snow.png": {
          this.enemyTile.setHasCollision(true);
          this.enemyTexture = MapHelper.loadTexture("snowEnemy.png");
          break;
      }
    }
  }


  @Override
  public World.EntityBuilder inject(World.EntityBuilder builder) {
    return builder
            .with(new Enemy(false))
            .with(new Position(this.xSpawn, this.ySpawn))
      .with(new Renderable(Set.of(ViewType.MAIN), 5,
        (Entity entity, RenderWindow window, World world) -> {

            //TODO: Get if specific enemy has been defeated
            //if(defeated == false) {
            Sprite en = new Sprite(this.enemyTexture);
            en.setPosition(this.xSpawn * 64, this.ySpawn * 64);
            window.draw(en);
            //}

        }));
  }
}