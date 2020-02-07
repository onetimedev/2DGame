package scc210game.game.components;

import scc210game.engine.ecs.Component;

public class Item extends Component {

    public final int itemID;
    public int level;

    public Item(int itemID, int level) {
        this.itemID = itemID;
        this.level = level;
    }

    @Override
    public String serialize(){
        return null;
    }
}
