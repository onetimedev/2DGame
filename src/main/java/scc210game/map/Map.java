package scc210game.map;

import scc210game.ecs.Component;
import scc210game.render.Render;

/**
 * Create map as part of the ECS
 */
public class Map extends Component {
	private Tile[][] mapTiles;
	private GenerateMap genMap;


	@Override
	public String serialize() {
		return null;
	}


	public Map() {
		genMap = new GenerateMap();
		mapTiles = genMap.getAllTiles();

		}

	public Tile[][] getMap() {
		return mapTiles;
	}

	public int getWidth() {
		return genMap.getMapSize().x;
	}

	public int getHeight() {
		return genMap.getMapSize().y;
	}

	public Tile getTile(int xPos, int yPos) {
		return mapTiles[xPos][yPos];
	}







	/**
	 * Method to create chests on tiles pseudo-random
	 * Creating an entity with position component, renderable component and inventory component
	 * giving position to entity based on the available spawnable tiles
	 */
	private void placeChests() {

	}



}
