package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.SerDe;
import scc210game.game.items.ItemData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Item extends Component {
    public final int itemID;
    public final String name;
    public int level;

    public final List<ItemData> itemDatas;

    public Item(int itemID, String name, int level, List<ItemData> itemDatas) {
        this.itemID = itemID;
        this.name = name;
        this.level = level;
        this.itemDatas = itemDatas;
    }

    public String tooltipString() {
        var s = new StringBuilder();
        s.append(this.name)
                .append("\n")
                .append("-".repeat(this.name.length()))
                .append("\n");

        for (var itemData: this.itemDatas) {
            s.append(itemData.infoData());
        }

        return s.toString();
    }

    static {
        register(Item.class, j -> {
            var json = (JsonObject) j;
            var itemID = (Integer) json.get("itemID");
            var name = (String) json.get("name");
            var level = (Integer) json.get("level");
            var itemDatasS = (JsonArray) json.get("itemDatas");
            var itemDatas = itemDatasS.stream().map(i -> SerDe.deserialize((Jsonable) i, ItemData.class)).collect(Collectors.toList());

            return new Item(itemID, name, level, itemDatas);
        });
    }

    @Override
    public Jsonable serialize(){
        return new JsonObject(Map.of(
                "itemID", this.itemID,
                "name", this.name,
                "level", this.level,
                "itemDatas", this.itemDatas.stream().map(ItemData::serialize).collect(Collectors.toList())
        ));
    }

    @Override
    public Item copy() {
        return new Item(this.itemID, this.name, this.level, itemDatas);
    }
}
