package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import java.io.IOException;
import java.nio.file.Paths;


public class Tile {
	private Texture tileTexture = new Texture();
	private Vector2i textureSize;
	private int xPos;
	private int yPos;
	private Boolean hasCollision;
	private String fileName;
	private Boolean canHaveChest;
	private Boolean canHaveEnemy;
	private Boolean hasEnemy;
	private Boolean canHaveStory;


	public Tile(String fn, int x, int y, Boolean collision, Boolean chest, Boolean enemy) {
		fileName = fn;
		String assetsPath = "./src/main/resources/textures/";
		xPos = x;
		yPos = y;
		hasCollision = collision;
		canHaveChest = chest;
		canHaveEnemy = enemy;
		canHaveStory = false;
		hasEnemy = false;

		try {
			tileTexture.loadFromFile(Paths.get(assetsPath, fileName));
			textureSize = tileTexture.getSize();
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to store a tile as a JSON object
	 * @return the tiles data as a JSON object
	 */
	public JsonObject serialize() {  // returns a JSON object representing the tile
		JsonObject json = new JsonObject();
		json.put("texture", fileName);
		json.put("x", xPos);
		json.put("y", yPos);
		json.put("collision", hasCollision);
		json.put("chest", canHaveChest);
		json.put("enemy", canHaveEnemy);
		return json;
	}


	/**
	 * Method to load a tile from a JSON object
	 * @param json the object containing the file values
	 * @return the created tile based on the JSON values
	 */
	public static Tile deserialize(JsonObject json) {  // changing JSON to Tile class
		String texture = (String) json.get("texture");
		int x = (int) json.get("x");
		int y = (int) json.get("y");
		Boolean hasCol = (Boolean) json.get("collision");
		Boolean hasCh = (Boolean) json.get("chest");
		Boolean hasEn = (Boolean) json.get("enemy");
		return new Tile(texture, x, y, hasCol, hasCh, hasEn);
	}

  public Vector2f getPosition() {
      return new Vector2f(xPos, yPos);
  }

  public int getXPos() {
      return xPos;
  }

  public int getYPos() {
      return yPos;
  }

  public Vector2i getXYPos() { return new Vector2i(xPos, yPos); }

  public Texture getTexture() {
      return tileTexture;
  }

  public String getTextureName() {
		return fileName;
  }

	public Boolean canHaveChest() {
		return canHaveChest;
	}

	public Boolean canHaveEnemy() { return canHaveEnemy; }

  public Boolean hasCollision() {
        return hasCollision;
    }

  public Boolean getHasEnemy() {
		return hasEnemy;
  }

  public void setHasEnemy(Boolean en) {
		hasEnemy = en;
  }

  public Vector2i getTextureSize() {
        return textureSize;
    }

  public void setTexture(String fn) {
	  String assetsPath = "./src/main/resources/textures/";
	  try {
		  tileTexture.loadFromFile(Paths.get(assetsPath, fn));
		  textureSize = tileTexture.getSize();
	  }
	  catch(IOException e) {
		  throw new RuntimeException(e);
	  }
  }

  public void setHasCollision(Boolean b) {
		hasCollision = b;
  }

	public void setCanHaveEnemy(Boolean b) {
		canHaveEnemy = b;
	}

	public void setCanHaveStory(Boolean b) {
		canHaveStory = b;
	}

	public boolean canHaveStory() {
		return canHaveStory;
	}

}
