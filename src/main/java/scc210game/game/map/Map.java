package scc210game.game.map;

import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Component;
import scc210game.game.utils.MapHelper;

import java.util.ArrayList;

/**
 * Create map as part of the ECS
 */
public class Map extends Component {
    private final Tile[][] mapTiles;
    private final GenerateMap genMap;
    private final ArrayList<Tile> enemyTiles;
    private final ArrayList<Tile> npcTiles;
    private final ArrayList<Vector2i[]> bossCoords;
    private final ArrayList<Tile> chestTiles;


    @Override
    public String serialize() {
        return null;
    }


    public Map() {
        this.genMap = new GenerateMap();
        this.mapTiles = this.genMap.getGenTiles();
        this.enemyTiles = this.genMap.getGenEnemyTiles();
        this.npcTiles = this.genMap.getGenNPCTiles();
        this.bossCoords = this.genMap.getBossCoords();
        this.chestTiles = this.genMap.getGenChestTiles();
    }


    public Tile[][] getMap() {
        return this.mapTiles;
    }

    public int getWidth() {
        return MapHelper.mapSize.x;
    }

    public int getHeight() {
        return MapHelper.mapSize.y;
    }

    public Tile getTile(int xPos, int yPos) {
        return this.mapTiles[xPos][yPos];
    }

    public boolean legalTile(int xPos, int yPos) {
        return xPos <= MapHelper.mapSize.x && xPos >= 0 && yPos <= MapHelper.mapSize.y && yPos >= 0;
    }

    public int getTileMaxX() {
        return this.mapTiles.length - 1;
    }

    public int getTileMaxY() {
        return this.mapTiles[0].length - 1;
    }

    public ArrayList<Tile> getEnemyTiles() {
        return this.enemyTiles;
    }

    public ArrayList<Tile> getNPCTiles() {
        return this.npcTiles;
    }

    public ArrayList<Vector2i[]> getBossCoords() {
        return this.bossCoords;
    }

    public ArrayList<Tile> getChestTiles() {
        return this.chestTiles;
    }

}
