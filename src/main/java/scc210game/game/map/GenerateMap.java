package scc210game.game.map;

import org.jsfml.graphics.Texture;
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
	private ArrayList<Vector2i> possNPCTiles = new ArrayList<>();
	private Vector2i[] enemyTiles;
	private Vector2i[] npcTiles;


	// Read from object map tile values that are already preset
	public GenerateMap() {
		mapSize = new Vector2i(120, 120);
		allTiles = new Tile[mapSize.x][mapSize.y];
		jsonToTiles();
		addEnemies();
		addNPCs();
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
					//System.out.println("[" + cnt + "]" + " Tile " + x + "," + y + " created. With texture: "  + tileValues.getInteger(cnt));
					allTiles[x][y] = Tile.deserialize(tileData(tileValues.getInteger(cnt), x, y));
					if(allTiles[x][y].canHaveEnemy() && allTiles[x][y].getTextureName().contains("enemy_"))
						possEnemyTiles.add(allTiles[x][y].getXYPos());

					if(allTiles[x][y].getTextureName().contains("story.png"))
						possNPCTiles.add(allTiles[x][y].getXYPos());

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
			case 23: {  // Enemy Basalt
				tileData.put("texture", "enemy_basalt.png");
				tileData.put("enemy", true);
				break;
			}
			case 24: {  // Enemy Sand
				tileData.put("texture", "enemy_sand.png");
				tileData.put("enemy", true);
				break;
			}
			case 25: {  // Enemy Grass
				tileData.put("texture", "enemy_grass.png");
				tileData.put("enemy", true);
				break;
			}
			case 26: {  // Enemy Snow
				tileData.put("texture", "enemy_snow.png");
				tileData.put("enemy", true);
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

	public Vector2i[] getNPCTiles() {
		return npcTiles;
	}


	/**
	 * 	Method that stores the boss enemy coordinates and returns them in an ArrayList
	 * 	0 = grass, 1 = water, 2 = fire, 3 = ice
	 * @return
	 */
	public ArrayList<Vector2i[]> getBossCoords() {
		Vector2i[] grassBoss = new Vector2i[4];
		grassBoss[0] = new Vector2i(8,65);
		grassBoss[1] = new Vector2i(9,65);
		grassBoss[2] = new Vector2i(8,66);
		grassBoss[3] = new Vector2i(9,66);

		Vector2i[] waterBoss = new Vector2i[4];
		waterBoss[0] = new Vector2i(25,23);
		waterBoss[1] = new Vector2i(26,23);
		waterBoss[2] = new Vector2i(25,24);
		waterBoss[3] = new Vector2i(26,24);

		Vector2i[] fireBoss = new Vector2i[4];
		fireBoss[0] = new Vector2i(107,5);
		fireBoss[1] = new Vector2i(108,5);
		fireBoss[2] = new Vector2i(107,6);
		fireBoss[3] = new Vector2i(108,6);

		Vector2i[] snowBoss = new Vector2i[4];
		snowBoss[0] = new Vector2i(101,100);
		snowBoss[1] = new Vector2i(102,100);
		snowBoss[2] = new Vector2i(101,101);
		snowBoss[3] = new Vector2i(102,101);

		ArrayList<Vector2i[]> allBossCoords = new ArrayList<>();
		allBossCoords.add(grassBoss);
		allBossCoords.add(waterBoss);
		allBossCoords.add(fireBoss);
		allBossCoords.add(snowBoss);


		/*
		* Looping through all boss coordinates and assigning the tile textures below the bosses to the
		* correct biome.
		*/
		for(int i=0; i< allBossCoords.size(); i++) {
			for (int j = 0; j < allBossCoords.get(i).length; j++) {
				allTiles[allBossCoords.get(i)[j].x][allBossCoords.get(i)[j].y].setHasCollision(true);
				allTiles[allBossCoords.get(i)[j].x][allBossCoords.get(i)[j].y].setHasEnemy(true);
				switch (i) {
					case 0: {
						allTiles[allBossCoords.get(i)[j].x][allBossCoords.get(i)[j].y].setTexture("grass2.png");
						break;
					}
					case 1: {
						allTiles[allBossCoords.get(i)[j].x][allBossCoords.get(i)[j].y].setTexture("sand.png");
						break;
					}
					case 2: {
						allTiles[allBossCoords.get(i)[j].x][allBossCoords.get(i)[j].y].setTexture("light_basalt.png");
						break;
					}
					case 3: {
						allTiles[allBossCoords.get(i)[j].x][allBossCoords.get(i)[j].y].setTexture("ice.png");
						break;
					}
				}
			}
		}

		return allBossCoords;
	}


	//TODO
	//private void addFinalBoss() {

	//}




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

	private void addNPCs() {
		ArrayList<Vector2i> npcList = new ArrayList<>();

		npcList.addAll(possNPCTiles);

		npcTiles = new Vector2i[npcList.size()];
		npcTiles = npcList.toArray(npcTiles);

	}


}
