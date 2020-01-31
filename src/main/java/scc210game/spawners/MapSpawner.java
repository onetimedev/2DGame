package scc210game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

import scc210game.ecs.Entity;
import scc210game.ecs.Query;
import scc210game.ecs.Spawner;
import scc210game.ecs.World;
import scc210game.map.Map;
import scc210game.map.Player;
import scc210game.movement.Position;
import scc210game.render.Renderable;
import scc210game.render.ViewType;

import java.util.Set;


public class MapSpawner implements Spawner {

  @Override
  public World.EntityBuilder inject(World.EntityBuilder builder) {
    return builder
      .with(new Map())
      .with(new Renderable(Set.of(ViewType.MAIN, ViewType.MINIMAP), 0,
      (Entity entity, RenderWindow window, World world) -> {
        Map m = world.fetchComponent(entity, Map.class);

        var playerEnt = world.applyQuery(Query.builder().require(Player.class).build()).findFirst().get();
        var position = world.fetchComponent(playerEnt, Position.class);
        Vector2f playerCoords = new Vector2f(position.xPos, position.yPos);

        // Number of tiles that can fit in windows X and Y
        int tilesX = (int) Math.ceil(window.getView().getSize().x / 64.0);
        int tilesY = (int) Math.ceil(window.getView().getSize().y / 64.0);
        //System.out.println("X fits: " + tilesX + " Y fits: " + tilesY);

        //System.out.println("View Size: " + window.getView().getSize());
        //System.out.println("View Center: " + window.getView().getCenter());
        //System.out.println("Window Size: " + window.getSize());

        int positionX = 0;  // Sprites X Position in window
        int positionY = 0;  // Sprites Y Position in window
        int tilesLeft = (int) Math.floor(tilesX / 2.0);  // Number of tiles left of player X coord
        int startX = (int) (playerCoords.x - tilesLeft);  // First tile X coord to be rendered from
        int tilesTop = (int) Math.floor(tilesY / 2.0);  // Number of tiles above player Y coord
        int startY = (int) (playerCoords.y - tilesTop);  // First tile Y coord to be rendered from

        if (startX < 0)
            startX = 0;
        else if (startX > m.getTileMaxX())
            startX = m.getTileMaxX();
        if (startY < 0)
            startY = 0;
        else if (startY > m.getTileMaxY())
            startY = m.getTileMaxY();




        // System.out.println("TilesX: " + tilesX);
        // System.out.println("TilesY: " + tilesY);

        // Goes through each X,Y coordinate around the player that can be rendered and
        // renders the tile at this X,Y coordinate
        int tileCount = 0;
        for (int x = 0; x < tilesX; x++) {
          for (int y = 0; y < tilesY; y++) {
            if (startX <= m.getTileMaxX() && startY <= m.getTileMaxY()) {  // Only render tile if its X,Y is valid
              //System.out.println("X: " + startX + " Y: " + startY);
              Sprite tile = new Sprite(m.getTile(startX, startY).getTexture());
              tile.setOrigin(window.getView().getSize().x/2, window.getView().getSize().y/2);
              tile.setPosition(positionX, positionY);
              System.out.println("["+ tileCount + "] " + "Tile " + startX + "," + startY + " at Position " + positionX + "," + positionY);
              window.draw(tile);
              tileCount++;
            }
            startX++;
            positionX += 64;
          }
          startX = (int) (playerCoords.x - tilesLeft);
          if(startX < 0)
            startX = 0;
          startY++;
          positionX = 0;
          positionY += 64;
        }

      }));
    }
}
