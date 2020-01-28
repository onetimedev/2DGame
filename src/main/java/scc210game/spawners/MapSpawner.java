package scc210game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2i;
import scc210game.ecs.Entity;
import scc210game.ecs.Spawner;
import scc210game.ecs.World;
import scc210game.map.Map;
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

                            Vector2i playerCoords = new Vector2i(40, 32);  //TODO: Player entity get current position coords

                            // Number of tiles that can fit in windows X and Y
                            int tilesX = (int) Math.ceil(window.getView().getSize().x / 64.0);
                            int tilesY = (int) Math.ceil(window.getView().getSize().y / 64.0);

                            int positionX = 0;  // Sprites X Position in window
                            int positionY = 0;  // Sprites Y Position in window
                            int tilesLeft = (int) Math.floor(tilesX / 2.0);  // Number of tiles left of player X coord
                            int startX = playerCoords.x - tilesLeft;  // First tile X coord to be rendered from
                            int tilesTop = (int) Math.floor(tilesY / 2.0);  // Number of tiles above player Y coord
                            int startY = playerCoords.y - tilesTop;  // First tile Y coord to be rendered from

                            if (startX < 0)
                                startX = 0;
                            else if (startX > m.getTileMaxX())
                                startX = m.getTileMaxX();
                            if (startY < 0)
                                startY = 0;
                            else if (startY > m.getTileMaxY())
                                startY = m.getTileMaxY();

//                            System.out.println("TilesX: " + tilesX);
//                            System.out.println("TilesY: " + tilesY);

                            // Goes through each X,Y coordinate around the player that can be rendered and
                            // renders the tile at this X,Y coordinate
                            for (int y = 0; y < tilesY; y++) {
                                for (int x = 0; x < tilesX; x++) {
                                    if (startX <= m.getTileMaxX() && startY <= m.getTileMaxY()) {  // Only render tile if its X,Y is valid
                                        Sprite tile = new Sprite(m.getTile(startX, startY).getTexture());
                                        tile.setPosition(positionX, positionY);
//                                        System.out.println("Tile " + startX + "," + startY + " at Position " + positionX + "," + positionY);
                                        window.draw(tile);
                                    }
                                    startX++;
                                    positionX += 64;
                                }
                                startX = playerCoords.x - tilesLeft;
                                startY++;
                                positionX = 0;
                                positionY += 64;
                            }


                            // Tiles currently changing scale on window size change
                            // need them to remain the same size

                        }));
    }
}
