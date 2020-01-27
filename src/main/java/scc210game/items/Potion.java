package scc210game.items;

public class Potion extends Item implements Consumable{
    protected int hpGained;
    public Potion(String name, int level, String desc, int hpGained) {
        super(name, level, desc);
        this.hpGained = hpGained;
    }

    @Override
    public void consume() {


    }
}
