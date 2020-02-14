package scc210game.items;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;


import java.util.ArrayList;
import java.util.Set;

public class Item extends ItemComponent {

    public ArrayList<Entity> itemsList = new ArrayList<>();
    private World world;

    public Item(int id, int level) {
        super(id, level);




    }
}














