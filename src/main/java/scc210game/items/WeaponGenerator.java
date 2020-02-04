package scc210game.items;
import org.jsfml.graphics.Texture;

import java.io.File;
import java.io.IOException;
import java.util.Random;
public class WeaponGenerator{
    private String[] material = new String[]{"Wooden", "Bronze", "Gold", "Steel"};
    private String[] weapon = new String[]{"Staff", "Sword", "Axe"};
    private String[] enchantment = new String[]{"Hellfire", "Frozen", "Alpha", "Fury"};
    private String[] named = new String[]{"Lazarus", "Poseidon", "Excalibur", "Kusanagi"};
    private String[] title = new String[]{"The Untamed", "The Cursed", "The Possessed", "The Hope",
            "The Enchanted", "The Unyielding", "The Blessed"};
    private String[] element = new String[]{"Flames", "Water", "Earth", "Ice"};
    private int x;
    private int y;
    private int z;
    public String randomised = new String();
    public String weaponLore = new String();
    public int damage;
    private Random r = new Random();
    public Texture t = new Texture();


    public WeaponGenerator(int level) throws IOException {

        createWeapon(level);
        generateSprite(randomised);

    }

    private String CommonWeapon(int level){

        if (level <= 10){
            x = r.nextInt(material.length);
            y = r.nextInt(weapon.length);
            z = r.nextInt(element.length);
            //randomised = material[x] + " " + weapon[y] + " " + element[z];
            randomised = material[1] + " " + weapon[1] + " " + element[1];
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

    private String rareWeapon(){
        x = r.nextInt(named.length);
        y = r.nextInt(title.length);
        z = r.nextInt(element.length);
        return randomised = named[x] + " " + title[y] + " " + element[z];

    }

    private void createWeapon(int level){
        CommonWeapon(level);

    }

    public String lore(String lore){
        weaponLore = lore;
        return weaponLore;

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
                    damage = 10;

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


}
