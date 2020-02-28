package scc210game.game.utils;

import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;
import scc210game.game.map.Tile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MapHelper {

	public static Vector2i mapSize = new Vector2i(120,120);

	/**
	 * Method used in enemy spawners to load the specific texture for the entity.
	 * @param fileName
	 * @return
	 */
	public static Texture loadTexture(String fileName) {
        try {
            Texture t = new Texture();
            t.loadFromFile(Paths.get("./src/main/resources/textures/", fileName));
            return t;
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
	}


	/**
	 * Method to update tiles, useful for enemies, bosses, barriers
	 * @param tilesToChange tiles that need updating
	 * @param newTexture name of the new texture
	 * @param collision of the tile
	 * @param enemy on the tile?
	 */
	public static void changeTiles(ArrayList<Tile> tilesToChange, String newTexture, boolean collision, boolean enemy) {
		for (Tile t: tilesToChange) {
			t.setTexture(loadTexture(newTexture), newTexture);
			t.setHasCollision(collision);
			t.setHasEnemy(enemy);
		}
	}


	/**
	 * Method to change the texture of a tile based on its biome
	 * @param t
	 */
	public static void setTileToBiome(Tile t) {
		if(t.getYPos() < 60 && t.getXPos() < 60) {
			t.setTexture(loadTexture("sand.png"), "sand.png");
		}
		else if(t.getYPos() < 60 && t.getXPos() > 60) {
			t.setTexture(loadTexture("light_basalt.png"), "light_basalt.png");
		}
		else if(t.getYPos() > 60 && t.getXPos() < 55) {
			t.setTexture(loadTexture("grass.png"), "grass.png");
		}
		else if(t.getYPos() > 60 && t.getXPos() > 40) {
			t.setTexture(loadTexture("snow.png"), "snow.png");
		}
		if((t.getYPos() == 49 && t.getXPos() == 112) || (t.getYPos() == 61 && t.getXPos() == 113)) {
			t.setTexture(loadTexture("grass.png"), "grass.png");
		}
	}


	public static int checkBiome(String t) {
		if(t.contains("grass"))
			return 0;
		else if(t.contains("sand"))
			return 1;
		else if(t.contains("snow") || t.contains("ice"))
			return 3;
		else if(t.contains("basalt"))
			return 2;
		else {
			return 5;
		}
	}


}
