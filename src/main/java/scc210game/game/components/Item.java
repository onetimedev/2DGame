package scc210game.game.components;

import scc210game.engine.ecs.Component;

public class Item extends Component {
    private static int lastEntID = 0;

    public final int itemID;
    public final String name;
    public int level;

    public Item(int itemID, String name, int level) {
        this.itemID = itemID;
        this.name = name;
        this.level = level;
    }

    public static Item makeWithLevel(String name, int level) {
        return new Item(lastEntID++, name, level);
    }

    @Override
    public String serialize(){
        return null;
    }

    @Override
    public Component clone() {
        return new Item(this.itemID, this.name, this.level);
    }
}
