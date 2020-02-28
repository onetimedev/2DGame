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
import scc210game.game.components.TextureStorage;
import scc210game.game.map.Tile;

import java.util.Set;

public class EnemySpawner implements Spawner {
    private final Tile enemyTile;
    private final int xSpawn;
    private final int ySpawn;
    private String enemyTexturePath;


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
          this.enemyTexturePath = "textures/fireEnemy.png";
          this.enemyTile.setHasCollision(true);
          break;
      }
      case "enemy_sand.png": {
          this.enemyTexturePath = "textures/waterEnemy.png";
          this.enemyTile.setHasCollision(true);
      }
      case "enemy_grass.png": {
          this.enemyTexturePath = "textures/grassEnemy.png";
          this.enemyTile.setHasCollision(true);
          break;
      }
      case "enemy_snow.png": {
          this.enemyTexturePath = "textures/snowEnemy.png";
          this.enemyTile.setHasCollision(true);
          break;
      }
    }
  }


    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        return builder
                .with(new Enemy(false))
                .with(new Position(this.xSpawn, this.ySpawn))
                .with(new TextureStorage(this.enemyTexturePath))
                .with(new Renderable(Set.of(ViewType.MAIN), 5,
                        //TODO: Get if specific enemy has been defeated
                        //if(defeated == false) {
                        //}
                        EnemySpawner::accept));
    }

    private static void accept(Entity entity, RenderWindow window, World world) {
        var p = world.fetchComponent(entity, Position.class);
        var t = world.fetchComponent(entity, TextureStorage.class);
        Sprite en = new Sprite(t.getTexture());
        en.setPosition(p.xPos * 64, p.yPos * 64);
        window.draw(en);
    }
}