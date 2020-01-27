package scc210game.items;

import scc210game.ecs.Entity;
import scc210game.ecs.World;



import java.util.ArrayList;

public class Item extends ItemComponent {


    public World world;
    public ArrayList<Entity> itemsList = new ArrayList<>();

    public Item(String name, int level, String desc) {
        super(name, level, desc);

        itemsList.add(world.entityBuilder()
                .with(new ItemComponent(name, level, desc))
                .build());


    }

}


