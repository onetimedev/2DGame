package scc210game.items;

import scc210game.ecs.Component;



public class ItemComponent extends Component{

    public String name;
    private int level;
    private String desc = "";

    public ItemComponent(String name, int level, String desc) {
        this.name = name;
        this.level = level;
        this.desc = desc;

    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }

    public String getDesc(){
        return desc;
    }


    @Override
    public String serialize(){
        return null;
    }


}
