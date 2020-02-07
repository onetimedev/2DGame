package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.game.map.Map;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.map.Player;

import java.util.Set;
import java.util.Vector;


public class MapSpawner implements Spawner {

  @Override
  public World.EntityBuilder inject(World.EntityBuilder builder) {
    return builder
      .with(new Map())
      .with(new Renderable(Set.of(ViewType.MAIN), 0,
      (Entity entity, RenderWindow window, World world) -> {
        Map m = world.fetchComponent(entity, Map.class);

        var playerEnt = world.applyQuery(Query.builder().require(Player.class).build()).findFirst().get();
        var position = world.fetchComponent(playerEnt, Position.class);
        Vector2f playerCoords = new Vector2f(position.xPos, position.yPos);

        // Number of tiles that can fit in windows X and Y
        Vector2f mapSize = window.getView().getSize();
        Vector2f origin = window.getView().getCenter();

        int mapLeft = (int) (origin.x - (mapSize.x /2)) /64;
        int mapRight = (int) (origin.x + (mapSize.x /2)) /64;
        int mapTop = (int) (origin.y - (mapSize.y /2)) /64;
        int mapBottom = (int) (origin.y + (mapSize.y /2)) /64;


        // Goes through each X,Y coordinate around the player that can be rendered and
        // renders the tile at this X,Y coordinate
        //int tileCount = 0;
        for (int y = 0; y <  m.getTileMaxY(); y++) {
          for (int x = 0; x < m.getTileMaxX(); x++) {
            if(x < mapLeft || x > mapRight || y < mapTop || y > mapBottom)
              continue;

            Sprite tile = new Sprite(m.getTile(x, y).getTexture());
            tile.setPosition(x*64, y*64);
            //System.out.println("["+ tileCount + "] " + "Texture: " + m.getTile(startX, startY).getTextureName() + " Tile " + startX + "," + startY + " at Position " + positionX + "," + positionY);
            window.draw(tile);
            //tileCount++;
            }

        }
      }));
    }
}
