public class Item {

    public String name;
    public int id;
    private int level;

    //contains methods to create entities with item components describe names
    //implements events moving items between two inventories
    //arraylist of entities all entities have item components in this array


    public Item(String name, int id, int level){
        this.name = name;
        this.id = id;
        this.level = level;
    }

    public Item(){

    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public void use(){

    }


















}

