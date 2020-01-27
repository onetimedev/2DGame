package scc210game.items;

public class Weapon extends Item{

    private int damage;
    private int level;
    private String element;
    private int speed;

    public Weapon(String name, int level, int damage, String desc, String element, int speed) {

        super(name, level, desc);
        this.damage = damage;
        this.element = element;
        this.speed = speed;

    }

    private void setDamage(int damage){
        this.damage = damage;
    }

    private int getDamage(){
        return damage;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }

    private void setElement(String element){
        this.element = element;
        //fire, water, earth

    }

    private String getElement(){
        return element;
    }

    private void setSpeed(int speed){
        this.speed = speed;
    }

    private int getSpeed(){
        return speed;
    }

}

