package scc210game.game.map;

import org.jsfml.system.Vector2i;
import com.github.cliftonlabs.json_simple.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class to Generate the maps tiles given mapdata.json.
 */
public class GenerateMap {

	private Tile[][] allTiles;
	private Vector2i mapSize;
	private ArrayList<Vector2i> possEnemyTiles = new ArrayList<>();
	private Vector2i[] enemyTiles;


	// Read from object map tile values that are already preset
	public GenerateMap() {
		mapSize = new Vector2i(120, 120);
		allTiles = new Tile[mapSize.x][mapSize.y];
		jsonToTiles();
		addEnemies();
	}


	/**
	 * Deserialises all tiles from mapdata.json and adds them to a 2D array of tiles.
	 */
	private void jsonToTiles() {
		try {
			FileReader fr = new FileReader("./mapdata.json");
			JsonObject jsonData = (JsonObject) Jsoner.deserialize(fr);
			JsonArray tileValues = (JsonArray) jsonData.get("data");

			int cnt = 0;  // Count to get each tile value from tileValues
			for(int y=0; y<mapSize.y; y++)
				for (int x=0; x<mapSize.x; x++) {
					System.out.println("[" + cnt + "]" + " Tile " + x + "," + y + " created. With texture: "  + tileValues.getInteger(cnt));
					allTiles[x][y] = Tile.deserialize(tileData(tileValues.getInteger(cnt), x, y));
					if(allTiles[x][y].canHaveEnemy() && allTiles[x][y].getTextureName().equals("enemy.png"))
						possEnemyTiles.add(allTiles[x][y].getXYPos());
					cnt++;
				}

		}
		catch(FileNotFoundException | JsonException e) {
			throw new RuntimeException();
		}
	}


	/**
	 * Creates a JSON object with the specific data for the tiles type
	 * @param tileType the number corresponding to the type of tile
	 * @param x the X position
	 * @param y the Y position
	 * @return a formatted JSON object representing the tile
	 */
	private JsonObject tileData(int tileType, int x, int y) {
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
			case 15: {  // Ice
				tileData.put("texture", "ice.png");
				break;
			}
			case 16: {  // Snowy Forest
				tileData.put("texture", "snow_forest.png");
				tileData.put("collision", true);
				break;
			}
			case 17: {  // Final Boss
				tileData.put("texture", "boss_final.png");
				tileData.put("collision", true);
				tileData.put("enemy", true);
				break;
			}
			case 18: {  // Fire Boss
				tileData.put("texture", "boss_fire.png");
				tileData.put("collision", true);
				tileData.put("enemy", true);
				break;
			}
			case 19: {  // Grass Boss
				tileData.put("texture", "boss_grass.png");
				tileData.put("collision", true);
				tileData.put("enemy", true);
				break;
			}
			case 20: {  // Water Boss
				tileData.put("texture", "boss_water.png");
				tileData.put("collision", true);
				tileData.put("enemy", true);
				break;
			}
			case 21: {  // Snow Boss
				tileData.put("texture", "boss_snow.png");
				tileData.put("collision", true);
				tileData.put("enemy", true);
				break;
			}
			case 22: {  // Barrier
				tileData.put("texture", "barrier.png");
				tileData.put("collision", true);
				break;
			}
		}
		return tileData;
	}


	public Tile[][] getAllTiles() {
		return allTiles;
	}

	public Vector2i getMapSize() {
		return mapSize;
	}

	public Vector2i[] getEnemyTiles() {
		return enemyTiles;
	}

	/*
		Method to assign the spawn points of enemies on the map based on other spawn points and
		the number of enemies to be place on the map.
	*/
	private void addEnemies() {
		int minEnemyTiles = 30;
		int checkWithin = 20;
		int placedCount = 0;
		ArrayList<Vector2i> tempList = new ArrayList<>();

		while (minEnemyTiles > placedCount) {
			for (Vector2i coords : possEnemyTiles) {
				int count = 0;
				int rng = (int) (Math.random() * 10 + 1);
				if (rng > 7) {
					System.out.println("Coords: " + coords);
					for (Vector2i altCoords : possEnemyTiles)
						if ((coords.x - checkWithin > altCoords.x || coords.x + checkWithin < altCoords.x) || (coords.y - checkWithin > altCoords.y || coords.y + checkWithin < altCoords.y))
							count++;

					if (count == possEnemyTiles.size() - 1) {
						allTiles[coords.x][coords.y].setTexture("enemySpawn.png");
						tempList.add(coords);
						placedCount++;
					}
				}
			}
			checkWithin -= 2;
		}

		enemyTiles = new Vector2i[tempList.size()];
		enemyTiles = tempList.toArray(enemyTiles);
	}


}
