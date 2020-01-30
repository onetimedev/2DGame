package scc210game.map;

import org.jsfml.system.Vector2i;
import com.github.cliftonlabs.json_simple.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class GenerateMap {

	private Tile[][] allTiles;
	private Vector2i mapSize;
	private int[] tileValues;

	// Read from object map tile values that are already preset
	public GenerateMap() {
		mapSize = new Vector2i(120, 120);
		allTiles = new Tile[mapSize.x][mapSize.y];
		jsonToTiles();
	}


	private void jsonToTiles() {
		try {
			FileReader fr = new FileReader("./mapdata.json");
			JsonObject jsonData = (JsonObject) Jsoner.deserialize(fr);
			JsonArray tileValues = (JsonArray) jsonData.get("data");

			int cnt = 0;  // Count to get each tile value from tileValues
			for(int y=0; y<mapSize.y; y++)
				for (int x=0; x<mapSize.x; x++) {
					System.out.println("Tile " + x + "," + y + " created. With texture: "  + tileValues.getInteger(cnt));
					allTiles[x][y] = Tile.deserialize(tileTypes(tileValues.getInteger(cnt), x, y));
					cnt++;
				}

		}
		catch(FileNotFoundException | JsonException e) {
			throw new RuntimeException();
		}
	}

	/*
	Texture index, texture name, collision, chest, enemy
	{1, "forest", true, false, false}, {2, "grass", false, false, false},
	{3, "path", false, false, false}, {4, "chest", true, true, false},
	{5, "enemy", true, false, true}, {6, "tree", true, false, false},
	{7, "story", false, false, false}, {8, "border", true, false, false}
	*/

	/**
	 * Creates a JSON object with the specific data for the tiles type
	 * @param tileType
	 * @param x
	 * @param y
	 * @return
	 */
	private JsonObject tileTypes(int tileType, int x, int y) {
		JsonObject tileData = new JsonObject();
		tileData.put("texture", "null");
		tileData.put("x", x);
		tileData.put("y", y);
		tileData.put("collision", false);
		tileData.put("chest", false);
		tileData.put("enemy", false);

		switch(tileType) {
			case 1: {  // Forest
				tileData.put("texture", "forest.png");
				tileData.put("collision", true);
				break;
			}
			case 2: {  // Grass
				tileData.put("texture", "grass2.png");
				break;
			}
			case 3: {  // Path
				tileData.put("texture", "path.png");
				break;
			}
			case 4: {  // Chest
				tileData.put("texture", "chest.png");
				tileData.put("collision", true);
				tileData.put("chest", true);
				break;
			}
			case 5: {  // Enemy
				tileData.put("texture", "enemy.png");
				tileData.put("collision", true);
				tileData.put("enemy", true);
				break;
			}
			case 6: {  // Tree
				tileData.put("texture", "tree.png");
				tileData.put("collision", true);
				break;
			}
			case 7: {  // Story
				tileData.put("texture", "story.png");
				break;
			}
			case 8: {  // Border
				tileData.put("texture", "border.png");
				tileData.put("collision", true);
				break;
			}
			case 9: {  // Snow
				tileData.put("texture", "snow.png");
				break;
			}
			case 10: {  // Water
				tileData.put("texture", "water.png");
				tileData.put("collision", true);
				break;
			}
			case 11: {  // Sand
				tileData.put("texture", "sand.png");
				break;
			}
			case 12: {  // Lava
				tileData.put("texture", "lava.png");
				tileData.put("collision", true);
				break;
			}
			case 13: {  // Basalt
				tileData.put("texture", "basalt.png");
				tileData.put("collision", true);
				break;
			}
			case 14: {  // Basalt_Light
				tileData.put("texture", "light_basalt.png");
				break;
			}

		}

		//System.out.println(tileData);
		return tileData;
	}


	public Tile[][] getAllTiles() {
		return allTiles;
	}

	public Vector2i getMapSize() {
		return mapSize;
	}


}
