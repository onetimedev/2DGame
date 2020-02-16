package scc210game.engine.utils;

import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;

import java.io.IOException;
import java.nio.file.Paths;

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
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}


}
