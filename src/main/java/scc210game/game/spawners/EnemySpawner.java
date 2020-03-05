package scc210game.game.spawners;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import scc210game.engine.animation.Animate;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.utils.ResourceLoader;
import scc210game.game.map.Enemy;
import scc210game.game.components.TextureStorage;
import scc210game.game.map.Tile;

import java.time.Duration;
import java.util.Set;

public class EnemySpawner implements Spawner {
    private final Texture t;
    private final int xSpawn;
    private final int ySpawn;
    private String enemyTexturePath;
    private int damage;

    private int id;



    public EnemySpawner(Tile tile, int dmg, int id) {
        this.id = id;
        this.setTexture(tile.getTextureName());  // Update enemyTexturePath
      try {
        this.t = new Texture();
        this.t.loadFromFile(ResourceLoader.resolve(enemyTexturePath));
      }
      catch (final Exception e) {
        throw new RuntimeException();
      }

      this.damage = dmg;
      tile.setHasEnemy(true);
      tile.setHasCollision(true);
      this.xSpawn = tile.getXPos();
      this.ySpawn = tile.getYPos();

    }


  public void setTexture(String type) {
    switch (type) {
      case "enemy_basalt.png": {
          this.enemyTexturePath = "textures/map/fireEnemy.png";
          break;
      }
      case "enemy_sand.png": {
          this.enemyTexturePath = "textures/map/waterEnemy.png";
          break;
      }
      case "enemy_grass.png": {
          this.enemyTexturePath = "textures/map/grassEnemy.png";
          break;
      }
      case "enemy_snow.png": {
          this.enemyTexturePath = "textures/map/snowEnemy.png";
          break;
      }
    }
  }


    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
      return builder
        .with(new Enemy(false, this.damage, this.id))
        .with(new FilledInventorySpawner())
        .with(new Position(this.xSpawn, this.ySpawn))
        .with(new TextureStorage(this.enemyTexturePath))
        .with(new Animate(Duration.ofMillis((400 * this.t.getSize().x) / 64 - 1), ((e, w) -> {
        }), true))
        .with(new Renderable(Set.of(ViewType.MAIN), 5,
          EnemySpawner::accept));
    }

    private static void accept(Entity entity, RenderWindow window, World world) {
      var enemy = world.fetchComponent(entity, Enemy.class);
      if(!enemy.defeated) {  //TODO: Get if specific enemy has been defeated

        var position = world.fetchComponent(entity, Position.class);
        var textureStorage = world.fetchComponent(entity, TextureStorage.class);
        var animation = world.fetchComponent(entity, Animate.class);

        Sprite en = new Sprite(textureStorage.getTexture());
        en.setPosition(position.xPos * 64, position.yPos * 64);

        var numFrames = (textureStorage.getTexture().getSize().x / 64);

        var frame = (int) Math.floor(animation.pctComplete * (float) numFrames);

        int frameRow = frame / 8;
        int frameCol = frame % 8;
        en.setTextureRect(new IntRect(frameCol * 64, frameRow * 64, 64, 64));

        window.draw(en);

      }
    }
}