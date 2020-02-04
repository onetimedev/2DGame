package scc210game.items;
import org.jsfml.graphics.Texture;

import java.io.File;
import java.io.IOException;
import java.util.Random;
public class WeaponGenerator{
    public String[] material = new String[]{"Wooden", "Bronze", "Gold", "Steel"};
    public String[] weapon = new String[]{"Staff", "Sword", "Axe"};

    public String[] enchantment = new String[]{"Hellfire", "Frozen", "Alpha", "Fury"};
    public String[] named = new String[]{"Lazarus", "Poseidon", "Excalibur", "Kusanagi"};
    public String[] title = new String[]{"The Untamed", "The Cursed", "The Possessed", "The Hope",
            "The Enchanted", "The Unyielding", "The Blessed"};
    public String[] element = new String[]{"Flames", "Water", "Earth"};

    public int x;
    public int y;
    public int z;
    public String randomised = new String();
    Random r = new Random();
    Texture t = new Texture();

    public WeaponGenerator(int level) throws IOException {

        createWeapon(level);
        generateSprite(randomised);


    }

    public String CommonWeapon(int level){

        if (level <= 10){
            x = r.nextInt(material.length);
            y = r.nextInt(weapon.length);
            z = r.nextInt(element.length);
            randomised = material[x] + " " + weapon[y] + " " + element[z];
        }
        else if (level >= 11 && level <= 20){
            x = r.nextInt(enchantment.length);
            y = r.nextInt(weapon.length);
            z = r.nextInt(enchantment.length);
            randomised = enchantment[x] + " " + weapon[y] + " " + element[z];
        }
        else{
            rareWeapon();
        }

        return randomised;
    }

    public String rareWeapon(){
        x = r.nextInt(named.length);
        y = r.nextInt(title.length);
        z = r.nextInt(element.length);
        return randomised = named[x] + " " + title[y] + " " + element[z];

    }

    private Texture generateSprite(String name) throws IOException {
        Texture t = new Texture();

        if (name.contains("Fire")){
            if (name.contains("Staff")){

                if (name.contains("Wooden")){

                }
                if (name.contains("Steel")){

                }
                if (name.contains("Bronze")){

                }
                if (name.contains("Gold")){

                }
            }
            if (name.contains("Axe")){
                if (name.contains("Wooden")){

                }
                if (name.contains("Steel")){

                }
                if (name.contains("Bronze")){

                }
                if (name.contains("Gold")){

                }

            }
            if (name.contains("Sword")){
                if (name.contains("Wooden")){

                    t.loadFromFile(new File("src/Basic-Sword.png").toPath());
                }
                if (name.contains("Steel")){

                }
                if (name.contains("Bronze")){

                }
                if (name.contains("Gold")){

                }

            }
            if (name.contains("Hellfire")){

            }
            if (name.contains("Frozen")){

            }
            if (name.contains("Alpha")){

            }
            if (name.contains("Fury")){

            }
            if (name.contains("Lazarus")){

            }
            if (name.contains("Poseidon")){

            }
            if (name.contains("Excalibur")){

            }
            if (name.contains("Kusanagi")){

            }
        }
        if (name.contains("Water")){
            if (name.contains("Staff")){
                if (name.contains("Wooden")){

                }
                if (name.contains("Steel")){

                }
                if (name.contains("Bronze")){

                }
                if (name.contains("Gold")){

                }
            }
            if (name.contains("Axe")){
                if (name.contains("Wooden")){

                }
                if (name.contains("Steel")){

                }
                if (name.contains("Bronze")){

                }
                if (name.contains("Gold")){

                }

            }
            if (name.contains("Sword")){
                if (name.contains("Wooden")){

                }
                if (name.contains("Steel")){

                }
                if (name.contains("Bronze")){

                }
                if (name.contains("Gold")){

                }

            }
            if (name.contains("Hellfire")){

            }
            if (name.contains("Frozen")){

            }
            if (name.contains("Alpha")){

            }
            if (name.contains("Fury")){

            }
            if (name.contains("Lazarus")){

            }
            if (name.contains("Poseidon")){

            }
            if (name.contains("Excalibur")){

            }
            if (name.contains("Kusanagi")){

            }
        }
        if (name.contains("Earth")){
            if (name.contains("Staff")){
                if (name.contains("Wooden")){

                }
                if (name.contains("Steel")){

                }
                if (name.contains("Bronze")){

                }
                if (name.contains("Gold")){

                }
            }
            if (name.contains("Axe")){
                if (name.contains("Wooden")){

                }
                if (name.contains("Steel")){

                }
                if (name.contains("Bronze")){

                }
                if (name.contains("Gold")){

                }

            }
            if (name.contains("Sword")){
                if (name.contains("Wooden")){

                }
                if (name.contains("Steel")){

                }
                if (name.contains("Bronze")){

                }
                if (name.contains("Gold")){

                }

            }
            if (name.contains("Hellfire")){

            }
            if (name.contains("Frozen")){

            }
            if (name.contains("Alpha")){

            }
            if (name.contains("Fury")){

            }
            if (name.contains("Lazarus")){

            }
            if (name.contains("Poseidon")){

            }
            if (name.contains("Excalibur")){

            }
            if (name.contains("Kusanagi")){

            }
        }

        return t;

    }

    private void createWeapon(int level){
        CommonWeapon(level);

    }

    public void lore(){

    }

}
