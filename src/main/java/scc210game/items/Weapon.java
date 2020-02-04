package scc210game.items;

import org.jsfml.graphics.Texture;

import java.io.IOException;

public class Weapon extends Item{


    private int level;
    private WeaponGenerator w;


    public Weapon(int id, int level){

        super(id, level);



    }

    public String getName(){
        return w.randomised;
    }


    private int getDamage(){
        return w.damage;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }

    public String getDesc(){
        return w.weaponLore;

    }

    public void getWeapon() throws IOException {
        w = new WeaponGenerator(level);

    }

    public Texture getTexture(){
        return w.t;
    }



}

