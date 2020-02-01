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
        int tilesX = (int) Math.ceil(window.getView().getSize().x / 64.0);
        int tilesY = (int) Math.ceil(window.getView().getSize().y / 64.0);
        System.out.println("X fits: " + tilesX + " Y fits: " + tilesY);

        System.out.println("View Size: " + window.getView().getSize());
        System.out.println("View Center: " + window.getView().getCenter());
        System.out.println("Window Size: " + window.getSize());

        int tilesLeft = (int) Math.floor(tilesX / 2.0);  // Number of tiles left of player X coord
        int startX = (int) (playerCoords.x - tilesLeft);  // First tile X coord to be rendered from
        int tilesTop = (int) Math.floor(tilesY / 2.0);  // Number of tiles above player Y coord
        int startY = (int) (playerCoords.y - tilesTop);  // First tile Y coord to be rendered from

        System.out.println("TilesX: " + tilesX);
        System.out.println("TilesY: " + tilesY);

        System.out.println("L: " + tilesLeft + ", R: " + (tilesX - tilesLeft) + ", T: " + tilesTop + ", B: " + (tilesY - tilesTop));

        System.out.println("View X: "+ window.getView().getSize().x);

        System.out.println("Max X: " + m.getTileMaxX());
        System.out.println("Max Y: " + m.getTileMaxY());

        if (startX < 0)
            startX = 0;
        else if (startX > m.getTileMaxX())
            startX = m.getTileMaxX() - tilesX;
        if (startY < 0)
            startY = 0;
        else if (startY > m.getTileMaxY())
            startY = m.getTileMaxY() - tilesY;



        int positionX = 0;  // Sprites X Position in window
        int positionY = 0;  // Sprites Y Position in window

        // Goes through each X,Y coordinate around the player that can be rendered and
        // renders the tile at this X,Y coordinate
        int tileCount = 0;
        for (int y = 0; y < tilesY; y++) {
          for (int x = 0; x < tilesX; x++) {
            if (startX <= m.getTileMaxX() && startY <= m.getTileMaxY()) {  // Only render tile if its X,Y is valid
              Sprite tile = new Sprite(m.getTile(startX, startY).getTexture());
              tile.setOrigin(window.getView().getSize().x/2, window.getView().getSize().y/2);
              tile.setPosition(positionX, positionY);
              System.out.println("["+ tileCount + "] " + "Texture: " + m.getTile(startX, startY).getTextureName() + " Tile " + startX + "," + startY + " at Position " + positionX + "," + positionY);
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
