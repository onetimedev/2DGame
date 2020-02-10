package scc210game.game.components;

import scc210game.engine.ecs.Component;

public class Item extends Component {
    private static int lastEntID = 0;

    public final int itemID;
    public int level;

    public Item(int itemID, int level) {
        this.itemID = itemID;
        this.level = level;
    }

    public static Item makeWithLevel(int level) {
        return new Item(lastEntID++, level);
    }

    @Override
    public String serialize(){
        return null;
    }

    @Override
    public Component clone() {
        return new Item(this.itemID, this.level);
    }
}
