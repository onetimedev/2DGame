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
import scc210game.game.components.TextureStorage;
import scc210game.game.map.Enemy;
import scc210game.game.map.Tile;

import java.time.Duration;
import java.util.Set;


/**
 * Class to create an enemy entity with its components
 */
public class EnemySpawner implements Spawner {
    private final Texture t;
    private final int xSpawn;
    private final int ySpawn;
    private String enemyTexturePath;
    private int damage;

    private int id;


  /**
   * Constructor to assign attributes that will be used in the creation of the entity
   * @param tile the tile to place the enemy on
   * @param dmg the amount of damage the enemy will do
   * @param id the number of the enemy
   */
    public EnemySpawner(Tile tile, int dmg, int id) {
        this.id = id;
        this.setTexture(tile.getTextureName());  // Update enemyTexturePath
      try {
          this.t = new Texture();
          this.t.loadFromStream(ResourceLoader.resolve(this.enemyTexturePath));
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


  /**
   * Method to set the texture path of the enemy
   * @param type the type of enemy texture to set
   */
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


  /**
   * Method to create and return the enemy as an entity with its components
   * @param builder the {@link World.EntityBuilder} to inject into
   * @param world the World the entity is being built in
   * @return the new enemy entity
   */
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


  /**
   * Method called for the rendering of the entity to the game window
   * @param entity the player entity
   * @param window the game window
   * @param world the world for this state
   */
    private static void accept(Entity entity, RenderWindow window, World world) {
      var enemy = world.fetchComponent(entity, Enemy.class);
      if(!enemy.defeated) {

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