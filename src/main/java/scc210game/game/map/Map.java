package scc210game.game.map;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Component;
import scc210game.game.utils.MapHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create map as part of the ECS
 */
public class Map extends Component {
    private final Tile[][] mapTiles;
    private final ArrayList<Tile> enemyTiles;
    private final ArrayList<Tile> npcTiles;
    private final ArrayList<Vector2i[]> bossCoords;
    private final ArrayList<Tile> chestTiles;

    static {
        register(Map.class, j -> {
            var json = (JsonObject) j;

            var mapTilesS = (JsonArray) json.get("mapTiles");
            var mapTiles = mapTilesS.stream()
                    .map(o -> {
                        var a = (JsonArray) o;
                        return a.stream()
                                .map(oT -> Tile.deserialize((JsonObject) oT))
                                .toArray(Tile[]::new);
                    })
                    .toArray(Tile[][]::new);

            var enemyTiles = ((JsonArray) json.get("enemyTiles"))
                    .stream()
                    .map(o -> Tile.deserialize((JsonObject) o))
                    .collect(Collectors.toCollection(ArrayList::new));

            var npcTiles = ((JsonArray) json.get("npcTiles"))
                    .stream()
                    .map(o -> Tile.deserialize((JsonObject) o))
                    .collect(Collectors.toCollection(ArrayList::new));

            var chestTiles = ((JsonArray) json.get("chestTiles"))
                    .stream()
                    .map(o -> Tile.deserialize((JsonObject) o))
                    .collect(Collectors.toCollection(ArrayList::new));

            var bossCoords = ((JsonArray) json.get("bossCoords"))
                    .stream()
                    .map(vaS -> ((JsonArray) vaS)
                            .stream()
                            .map(vS -> {
                                var a = (JsonArray) vS;
                                var x = (Integer) a.get(0);
                                var y = (Integer) a.get(1);

                                return new Vector2i(x, y);
                            })
                            .toArray(Vector2i[]::new)
                    )
                    .collect(Collectors.toCollection(ArrayList::new));

            return new Map(mapTiles, enemyTiles, npcTiles, bossCoords, chestTiles);
        });
    }


    @Override
    public Jsonable serialize() {
        var mapTilesS = Arrays.stream(mapTiles)
                .map(a -> Arrays.stream(a)
                        .map(Tile::serialize)
                        .collect(Collectors.toCollection(JsonArray::new)))
                .collect(Collectors.toCollection(JsonArray::new));

        var enemyTilesS = enemyTiles.stream()
                .map(Tile::serialize)
                .collect(Collectors.toCollection(JsonArray::new));

        var npcTilesS = npcTiles.stream()
                .map(Tile::serialize)
                .collect(Collectors.toCollection(JsonArray::new));

        var chestTilesS = chestTiles.stream()
                .map(Tile::serialize)
                .collect(Collectors.toCollection(JsonArray::new));

        var bossCoordsS = bossCoords.stream()
                .map(va -> Arrays.stream(va)
                        .map(v -> new JsonArray(List.of(v.x, v.y)))
                        .collect(Collectors.toCollection(JsonArray::new)))
                .collect(Collectors.toCollection(JsonArray::new));

        return new JsonObject(java.util.Map.of(
                "mapTiles", mapTilesS,
                "enemyTiles", enemyTilesS,
                "npcTiles", npcTilesS,
                "chestTiles", chestTilesS,
                "bossCoords", bossCoordsS));
    }

    public Map() {
        GenerateMap genMap = new GenerateMap();
        this.mapTiles = genMap.getGenTiles();
        this.enemyTiles = genMap.getGenEnemyTiles();
        this.npcTiles = genMap.getGenNPCTiles();
        this.bossCoords = genMap.getBossCoords();
        this.chestTiles = genMap.getGenChestTiles();
    }

    Map(Tile[][] mapTiles, ArrayList<Tile> enemyTiles, ArrayList<Tile> npcTiles, ArrayList<Vector2i[]> bossCoords, ArrayList<Tile> chestTiles) {
        this.mapTiles = mapTiles;
        this.enemyTiles = enemyTiles;
        this.npcTiles = npcTiles;
        this.bossCoords = bossCoords;
        this.chestTiles = chestTiles;
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
