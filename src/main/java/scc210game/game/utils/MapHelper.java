package scc210game.game.utils;

import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;
import scc210game.game.map.Tile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MapHelper {

    public static Vector2i mapSize = new Vector2i(120, 120);

    /**
     * Method used in enemy spawners to load the specific texture for the entity.
     *
     * @param fileName
     * @return
     */
    public static Texture loadTexture(String fileName) {
        try {
            Texture t = new Texture();
            t.loadFromFile(Paths.get("./src/main/resources/textures/", fileName));
            return t;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Method to update tiles, useful for enemies, bosses, barriers
     *
     * @param tilesToChange tiles that need updating
     * @param newTexture    name of the new texture
     * @param collision     of the tile
     * @param enemy         on the tile?
     */
    public static void changeTiles(ArrayList<Tile> tilesToChange, String newTexture, boolean collision, boolean enemy) {
        for (final Tile t : tilesToChange) {
            t.setTexture(newTexture);
            t.setHasCollision(collision);
            t.setHasEnemy(enemy);
        }


    }


}
