package scc210game.map;

import scc210game.ecs.Component;
import scc210game.render.Render;

/**
 * Create map as part of the ECS
 */
public class Map extends Component {
	private Tile[][] mapTiles;


	@Override
	public String serialize() {
		return null;
	}


	public Map() {
		GenerateMap genMap = new GenerateMap(10, 10);
		mapTiles = genMap.getAllTiles();

		}





	/**
	 * Method to create chests on tiles pseudo-random
	 * Creating an entity with position component, renderable component and inventory component
	 * giving position to entity based on the available spawnable tiles
	 */
	private void placeChests() {

	}



}
