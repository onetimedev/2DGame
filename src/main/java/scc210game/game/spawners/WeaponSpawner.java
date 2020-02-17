package scc210game.game.spawners;

import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.utils.ResourceLoader;
import scc210game.game.components.Item;
import scc210game.game.components.TextureStorage;
import scc210game.game.items.Element;
import scc210game.game.items.Material;
import scc210game.game.items.Type;
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
        var element = randomEnum(Element.class);
        var name = generateWeaponName(this.level, element);
        var lore = generateLore(this.level);
        var tex = generateTexture(this.level, element);

        return builder
                .with(Item.makeWithLevel(name, this.level, List.of(new Weapon(dmg, lore, element))))
                .with(new TextureStorage(tex));
    }

    private static String[] possibleEnchantment = new String[]{"Hellfire", "Frozen", "Alpha", "Fury"};
    private static String[] possibleNamed = new String[]{"Lazarus", "Poseidon", "Excalibur", "Kusanagi"};
    private static String[] possibleTitle = new String[]{"The Untamed", "The Cursed", "The Possessed", "The Hope",
            "The Enchanted", "The Unyielding", "The Blessed"};

    private static Random rng = new Random();

    private static <T> T randomElem(T[] arr) {
        var idx = rng.nextInt(arr.length);
        return arr[idx];
    }

    private static <E extends Enum<E>> E randomEnum(Class<E> clazz) {
        return randomElem(clazz.getEnumConstants());
    }

    private static int generateDamage(int level) {
        // min damage of 10, increases by level * random between 0.8 - 1.0 * 10
        var scale = 1.0f - 0.2f * rng.nextFloat();
        return (int) (level * scale * 10 + 10);
    }

    private static String getSwordTextureName(Element e) {
        switch (e) {
            case FLAMES:
                return "Fire-Sword.png";
            case WATER:
                return "Water-Sword.png";
            case EARTH:
                return "Earth-Sword.png";
            case ICE:
            default:
                return "Basic-Sword.png";
        }
    }

    private static Texture generateTexture(int level, Element element) {
        var tex = new Texture();
        try {
            tex.loadFromFile(ResourceLoader.resolve("textures/" + getSwordTextureName(element)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tex;
    }

    private static String generateWeaponName(int level, Element e){
        if (level <= 10){
            return randomEnum(Material.class).name + " " +
                    randomEnum(Type.class).name + " of " +
                    e.name;
        }
        else if (level <= 20){
            return randomElem(possibleEnchantment) + " " +
                    randomEnum(Type.class).name + " of " +
                    e.name;
        }
        else{
            return randomElem(possibleNamed) + " " +
                    randomElem(possibleTitle) + " " +
                    randomEnum(Type.class).name + " of " +
                    e.name;
        }
    }

    private static String generateLore(int level) {
        return "";
    }
}
