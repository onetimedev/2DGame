package scc210game.game.components;

import scc210game.engine.ecs.Component;
import scc210game.game.items.ItemData;

import java.util.List;

public class Item extends Component {
    private static int lastEntID = 0;

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

    public static Item makeWithLevel(String name, int level, List<ItemData> itemDatas) {
        return new Item(lastEntID++, name, level, itemDatas);
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

    @Override
    public String serialize(){
        return null;
    }

    @Override
    public Item copy() {
        return new Item(this.itemID, this.name, this.level, itemDatas);
    }
}
