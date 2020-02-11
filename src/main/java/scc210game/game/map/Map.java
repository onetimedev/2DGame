package scc210game.game.map;

import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Component;

import java.util.ArrayList;

/**
 * Create map as part of the ECS
 */
public class Map extends Component {
  private Tile[][] mapTiles;
  private GenerateMap genMap;
  private Tile[] enemyTiles;
  private ArrayList<Vector2i[]> bossCoords;


  @Override
  public String serialize() {
      return null;
  }


  public Map() {
      genMap = new GenerateMap();
      mapTiles = genMap.getAllTiles();
      enemyTiles = enemyTiles(genMap.getEnemyTiles());
      bossCoords = genMap.getBossCoords();
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

	private Tile[] enemyTiles(Vector2i[] eTiles) {
  	Tile[] tempTiles = new Tile[eTiles.length];
		for(int i=0; i < tempTiles.length; i++) {
			tempTiles[i] = getTile(eTiles[i].x, eTiles[i].y);
		}
  	return tempTiles;
	}

	public Tile[] getEnemyTiles() {
      return enemyTiles;
    }

  public ArrayList<Vector2i[]> getBossCoords() {
  	return bossCoords;
  }


}
