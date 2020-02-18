package scc210game.game.map;

import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Component;
import scc210game.game.utils.MapHelper;

import java.util.ArrayList;

/**
 * Create map as part of the ECS
 */
public class Map extends Component {
  private Tile[][] mapTiles;
  private GenerateMap genMap;
  private ArrayList<Tile> enemyTiles;
  private ArrayList<Tile> npcTiles;
  private ArrayList<Vector2i[]> bossCoords;
  private ArrayList<Tile> chestTiles;


  @Override
  public String serialize() {
      return null;
  }


  public Map() {
      genMap = new GenerateMap();
      mapTiles = genMap.getGenTiles();
      enemyTiles = genMap.getGenEnemyTiles();
      npcTiles = genMap.getGenNPCTiles();
      bossCoords = genMap.getBossCoords();
      chestTiles = genMap.getGenChestTiles();
  }


  public Tile[][] getMap() {
      return mapTiles;
  }

  public int getWidth() {
      return MapHelper.mapSize.x;
  }

  public int getHeight() {
      return MapHelper.mapSize.y;
  }

	public Tile getTile(int xPos, int yPos) {
		return mapTiles[xPos][yPos];
	}

	public boolean legalTile(int xPos, int yPos) {
  	if(xPos > MapHelper.mapSize.x || xPos < 0 || yPos > MapHelper.mapSize.y || yPos < 0)
  		return false;
  	return true;
	}

	public int getTileMaxX() {
		return mapTiles.length-1;
	}

	public int getTileMaxY() {
		return mapTiles[0].length-1;
	}

	public ArrayList<Tile> getEnemyTiles() {
      return enemyTiles;
    }

  public ArrayList<Tile> getNPCTiles() {
      return npcTiles;
    }

  public ArrayList<Vector2i[]> getBossCoords() {
  	return bossCoords;
  }

	public ArrayList<Tile> getChestTiles() {
		return chestTiles;
	}

}
