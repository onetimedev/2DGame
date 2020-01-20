package scc210game.map;

import org.jsfml.system.Vector2i;

public class GenerateMap {

	private Tile[][] allTiles = new Tile[10][10];
	private Vector2i mapSize;

	// Read from object map tile values that are already preset


	public GenerateMap() {
		mapSize = new Vector2i(10, 10);
		generate();
	}



	public Tile[][] getAllTiles() {
		return allTiles;
	}


	public Vector2i getMapSize() {
		return mapSize;
	}

	/**
	 * Method to generate all tiles, TEMP fixed size and texture
	 */
	private void generate() {
		for(int x=0; x<mapSize.x; x++)
			for(int y=0; y<mapSize.y; y++) {
				allTiles[x][y] = new Tile("grass.png", x, y, true);
			}
	}







}
