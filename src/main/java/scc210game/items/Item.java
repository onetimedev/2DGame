package scc210game.items;

import scc210game.ecs.Entity;
import scc210game.ecs.World;



import java.util.ArrayList;

public class Item extends ItemComponent {


    public World world;
    public ArrayList<Entity> itemsList = new ArrayList<>();

    public Item(int id, int level) {
        super(id, level);

        itemsList.add(world.entityBuilder()
                .with(new ItemComponent(id, level))
                .build());


    }

}


