package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonObject;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import scc210game.game.utils.MapHelper;

import java.io.IOException;
import java.nio.file.Paths;


public class Tile {
    private Texture tileTexture = new Texture();
    private Vector2i textureSize;
    private final int xPos;
    private final int yPos;
    private Boolean hasCollision;
    private final String fileName;
    private final Boolean canHaveChest;
    private Boolean canHaveEnemy;
    private Boolean hasEnemy;
    private Boolean canHaveStory;


    public Tile(String fn, int x, int y, Boolean collision, Boolean chest, Boolean enemy) {
        this.fileName = fn;
        String assetsPath = "./src/main/resources/textures/";
        this.xPos = x;
        this.yPos = y;
        this.hasCollision = collision;
        this.canHaveChest = chest;
        this.canHaveEnemy = enemy;
        this.canHaveStory = false;
        this.hasEnemy = false;

        try {
            this.tileTexture.loadFromFile(Paths.get(assetsPath, this.fileName));
            this.textureSize = this.tileTexture.getSize();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }


	/**
	 * Method to store a tile as a JSON object
	 * @return the tiles data as a JSON object
	 */
	public JsonObject serialize() {  // returns a JSON object representing the tile
    JsonObject json = new JsonObject();
    json.put("texture", this.fileName);
    json.put("x", this.xPos);
    json.put("y", this.yPos);
    json.put("collision", this.hasCollision);
    json.put("chest", this.canHaveChest);
    json.put("enemy", this.canHaveEnemy);
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
      return new Vector2f(this.xPos, this.yPos);
  }

  public int getXPos() {
      return this.xPos;
  }

  public int getYPos() {
      return this.yPos;
  }

  public Vector2i getXYPos() {
      return new Vector2i(this.xPos, this.yPos);
  }

  public Texture getTexture() {
      return this.tileTexture;
  }

  public String getTextureName() {
      return this.fileName;
  }

  public Boolean canHaveChest() {
      return this.canHaveChest;
  }

  public Boolean canHaveEnemy() {
      return this.canHaveEnemy;
  }

  public Boolean hasCollision() {
      return this.hasCollision;
  }

  public Boolean getHasEnemy() {
      return this.hasEnemy;
  }

  public void setHasEnemy(Boolean en) {
      this.hasEnemy = en;
  }

  public Vector2i getTextureSize() {
      return this.textureSize;
  }

  public void setTexture(String fn) {
	  String assetsPath = "./src/main/resources/textures/";
		this.tileTexture = MapHelper.loadTexture(fn);
		this.textureSize = this.tileTexture.getSize();
  }

  public void setHasCollision(Boolean b) {
      this.hasCollision = b;
  }

	public void setCanHaveEnemy(Boolean b) {
        this.canHaveEnemy = b;
	}

	public void setCanHaveStory(Boolean b) {
        this.canHaveStory = b;
	}

	public boolean canHaveStory() {
        return this.canHaveStory;
    }

}
