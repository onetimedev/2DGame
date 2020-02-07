package scc210game.game.map;

import scc210game.engine.ecs.Component;

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

	public int getTileMaxX() {
		return mapTiles.length-1;
	}

	public int getTileMaxY() {
		return mapTiles[0].length-1;
	}


}
