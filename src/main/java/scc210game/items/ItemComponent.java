package scc210game.items;

import scc210game.ecs.Component;



public class ItemComponent extends Component{


    public int id;
    private int level;

    public ItemComponent(int id, int level) {

        this.id =id;
        this.level = level;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }


    @Override
    public String serialize(){
        return null;
    }


}
