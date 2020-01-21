package scc210game.map;

import org.jsfml.system.Vector2i;

public class GenerateMap {

	private Tile[][] allTiles;
	private Vector2i mapSize;

	// Read from object map tile values that are already preset


	public GenerateMap() {
		mapSize = new Vector2i(120, 120);
		allTiles = new Tile[mapSize.x][mapSize.y];
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
				allTiles[x][y] = new Tile("grass.png", x, y, true, false, false);
			}
	}


	// Call Tile serialise in a method that reads a file
	// turn file into JSON array using JSON decode (JSON array of Jsonable)
	// Cast jsonable to JSON array, iterate over this (each element will be a JSONable)
	// Cast that jsonable to a json object, then can do tile.deserialize

	// Streams to deserialize, rather than explicit loop can map over a stream of json objects
	// jsonarray .toStream as its a collection

	/*
		File structure needs to match format of deserialise as if it was originally serialised by the
		program.

		Store file in working directory, same place as build.gradle etc
	 */






}
