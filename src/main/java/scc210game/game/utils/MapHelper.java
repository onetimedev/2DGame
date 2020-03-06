package scc210game.game.utils;

import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;
import scc210game.engine.utils.ResourceLoader;
import scc210game.game.map.Tile;

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
			t.loadFromStream(ResourceLoader.resolve("textures/map/" + fileName));
			return t;
		}
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
	}


	/**
	 * Method to update tiles, useful for enemies, bosses, barriers
	 * @param tilesToChange tiles that need updating
	 * @param newTextureName name of the new texture
	 * @param collision of the tile
	 * @param enemy on the tile?
	 */
	public static void changeTiles(ArrayList<Tile> tilesToChange, String newTextureName, boolean collision, boolean enemy) {
		for (final Tile t : tilesToChange) {
			t.setTexture(newTextureName);
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
			t.setTexture("sand.png");
		}
		else if(t.getYPos() < 60 && t.getXPos() > 60) {
			t.setTexture("light_basalt.png");
		}
		else if(t.getYPos() > 60 && t.getXPos() < 55) {
			t.setTexture("grass.png");
		}
		else if(t.getYPos() > 60 && t.getXPos() > 40) {
			t.setTexture("snow.png");
		}
		if((t.getYPos() == 49 && t.getXPos() == 112) || (t.getYPos() == 61 && t.getXPos() == 113) || (t.getYPos() == 48 && t.getXPos() == 93)) {
			t.setTexture("grass.png");
		}
	}


	/**
	 * Method to translate the biome of a given string to an integer
	 * @param t
	 * @return 0=Grass, 1=Sand, 2=Fire, 3=Snow, 5=None
	 */
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
