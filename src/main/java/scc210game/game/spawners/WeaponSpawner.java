package scc210game.game.spawners;

import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.utils.ResourceLoader;
import scc210game.game.components.Item;
import scc210game.game.components.TextureStorage;
import scc210game.game.items.Weapon;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class WeaponSpawner implements Spawner {
    private final int level;

    public WeaponSpawner(int level) {
        this.level = level;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        var dmg = generateDamage(this.level);
        var name = generateWeaponName(this.level);
        var lore = generateLore(this.level);
        var tex = generateTexture(this.level);

        return builder
                .with(Item.makeWithLevel(name, this.level, List.of(new Weapon(dmg, lore))))
                .with(new TextureStorage(tex));
    }

    private static String[] possibleMaterial = new String[]{"Wooden", "Bronze", "Gold", "Steel"};
    private static String[] possibleWeaponType = new String[]{"Staff", "Sword", "Axe"};
    private static String[] possibleEnchantment = new String[]{"Hellfire", "Frozen", "Alpha", "Fury"};
    private static String[] possibleNamed = new String[]{"Lazarus", "Poseidon", "Excalibur", "Kusanagi"};
    private static String[] possibleTitle = new String[]{"The Untamed", "The Cursed", "The Possessed", "The Hope",
            "The Enchanted", "The Unyielding", "The Blessed"};
    private static String[] possibleElement = new String[]{"Flames", "Water", "Earth", "Ice"};

    private static Random rng = new Random();

    private static <T> T randomElem(T[] arr) {
        var idx = rng.nextInt(arr.length);
        return arr[idx];
    }

    private static int generateDamage(int level) {
        // min damage of 10, increases by level * random between 0.8 - 1.0 * 10
        var scale = 1.0f - 0.2f * rng.nextFloat();
        return (int) (level * scale * 10 + 10);
    }

    private static Texture generateTexture(int level) {
        var tex = new Texture();
        try {
            tex.loadFromFile(ResourceLoader.resolve("textures/Basic-Sword.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tex;
    }

    private static String generateWeaponName(int level){
        if (level <= 10){
            return randomElem(possibleMaterial) + " " +
                    randomElem(possibleWeaponType) + " of " +
                    randomElem(possibleElement);
        }
        else if (level <= 20){
            return randomElem(possibleEnchantment) + " " +
                    randomElem(possibleWeaponType) + " of " +
                    randomElem(possibleElement);
        }
        else{
            return randomElem(possibleNamed) + " " +
                    randomElem(possibleTitle) + " " +
                    randomElem(possibleWeaponType) + " of " +
                    randomElem(possibleElement);
        }
    }

    private static String generateLore(int level) {
        return "";
    }

//    private TextureStorage generateSprite(String name) throws IOException {
//        TextureStorage t = new TextureStorage();
//
//        if (name.contains("Fire")){
//            if (name.contains("Staff")){
//
//                if (name.contains("Wooden")){
//
//                }
//                if (name.contains("Steel")){
//
//                }
//                if (name.contains("Bronze")){
//
//                }
//                if (name.contains("Gold")){
//
//                }
//            }
//            if (name.contains("Axe")){
//                if (name.contains("Wooden")){
//
//                }
//                if (name.contains("Steel")){
//
//                }
//                if (name.contains("Bronze")){
//
//                }
//                if (name.contains("Gold")){
//
//                }
//
//            }
//            if (name.contains("Sword")){
//                if (name.contains("Wooden")){
//
//                    t.loadFromFile(new File("src/Basic-Sword.png").toPath());
//                    damage = 10;
//
//                }
//                if (name.contains("Steel")){
//
//                }
//                if (name.contains("Bronze")){
//
//                }
//                if (name.contains("Gold")){
//
//                }
//
//            }
//            if (name.contains("Hellfire")){
//
//            }
//            if (name.contains("Frozen")){
//
//            }
//            if (name.contains("Alpha")){
//
//            }
//            if (name.contains("Fury")){
//
//            }
//            if (name.contains("Lazarus")){
//
//            }
//            if (name.contains("Poseidon")){
//
//            }
//            if (name.contains("Excalibur")){
//
//            }
//            if (name.contains("Kusanagi")){
//
//            }
//        }
//        if (name.contains("Water")){
//            if (name.contains("Staff")){
//                if (name.contains("Wooden")){
//
//                }
//                if (name.contains("Steel")){
//
//                }
//                if (name.contains("Bronze")){
//
//                }
//                if (name.contains("Gold")){
//
//                }
//            }
//            if (name.contains("Axe")){
//                if (name.contains("Wooden")){
//
//                }
//                if (name.contains("Steel")){
//
//                }
//                if (name.contains("Bronze")){
//
//                }
//                if (name.contains("Gold")){
//
//                }
//
//            }
//            if (name.contains("Sword")){
//                if (name.contains("Wooden")){
//
//                }
//                if (name.contains("Steel")){
//
//                }
//                if (name.contains("Bronze")){
//
//                }
//                if (name.contains("Gold")){
//
//                }
//
//            }
//            if (name.contains("Hellfire")){
//
//            }
//            if (name.contains("Frozen")){
//
//            }
//            if (name.contains("Alpha")){
//
//            }
//            if (name.contains("Fury")){
//
//            }
//            if (name.contains("Lazarus")){
//
//            }
//            if (name.contains("Poseidon")){
//
//            }
//            if (name.contains("Excalibur")){
//
//            }
//            if (name.contains("Kusanagi")){
//
//            }
//        }
//        if (name.contains("Earth")){
//            if (name.contains("Staff")){
//                if (name.contains("Wooden")){
//
//                }
//                if (name.contains("Steel")){
//
//                }
//                if (name.contains("Bronze")){
//
//                }
//                if (name.contains("Gold")){
//
//                }
//            }
//            if (name.contains("Axe")){
//                if (name.contains("Wooden")){
//
//                }
//                if (name.contains("Steel")){
//
//                }
//                if (name.contains("Bronze")){
//
//                }
//                if (name.contains("Gold")){
//
//                }
//
//            }
//            if (name.contains("Sword")){
//                if (name.contains("Wooden")){
//
//                }
//                if (name.contains("Steel")){
//
//                }
//                if (name.contains("Bronze")){
//
//                }
//                if (name.contains("Gold")){
//
//                }
//
//            }
//            if (name.contains("Hellfire")){
//
//            }
//            if (name.contains("Frozen")){
//
//            }
//            if (name.contains("Alpha")){
//
//            }
//            if (name.contains("Fury")){
//
//            }
//            if (name.contains("Lazarus")){
//
//            }
//            if (name.contains("Poseidon")){
//
//            }
//            if (name.contains("Excalibur")){
//
//            }
//            if (name.contains("Kusanagi")){
//
//            }
//        }
//
//        return t;
//
//    }
//
}
