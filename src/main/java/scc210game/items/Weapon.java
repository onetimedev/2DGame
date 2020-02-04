package scc210game.items;

import org.jsfml.graphics.Texture;

import java.io.IOException;

public class Weapon extends Item{

    private int damage;
    private int level;
    private String desc;
    WeaponGenerator w;


    public Weapon(int id, int level, int damage, String desc){

        super(id, level);


        this.damage = damage;
        this.desc = desc;

    }

    public String getName(){
        return w.randomised;
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

    public String getDesc(){
        return desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public void getWeapon() throws IOException {
        w = new WeaponGenerator(level);

    }

    public Texture getTexture(){
        return w.t;
    }



}

