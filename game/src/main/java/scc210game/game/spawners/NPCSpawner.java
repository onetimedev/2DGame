package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.game.map.NPC;
import scc210game.game.components.TextureStorage;
import scc210game.game.map.Tile;
import scc210game.game.utils.MapHelper;

import java.util.Set;

public class NPCSpawner implements Spawner {
    private final Tile npcTile;
    private final int xSpawn;
    private final int ySpawn;
    private String textureName;

    public NPCSpawner(Tile t) {
        npcTile = t;
        xSpawn = this.npcTile.getXPos();
        ySpawn = this.npcTile.getYPos();
        npcTile.setCanHaveStory(true);
        npcTile.setHasCollision(true);
        MapHelper.setTileToBiome(this.npcTile);
        textureName = setTexture(MapHelper.checkBiome(npcTile.getTextureName()));
    }


    public String setTexture(int type) {
        switch (type) {
            case 0: {
                return "storyGrass.png";
            }
            case 1: {
                return "storyWater.png";
            }
            case 3: {
                return "storySnow.png";
            }
            case 2: {
                return "storyFire.png";
            }
        }
        return "story.png";
    }


    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        return builder
                .with(new NPC())
                .with(new Position(this.xSpawn, this.ySpawn))
                .with(new TextureStorage("textures/map/" + textureName))
                .with(new Renderable(Set.of(ViewType.MAIN), 5,
                        NPCSpawner::accept));
    }

    private static void accept(Entity entity, RenderWindow window, World world) {
        var p = world.fetchComponent(entity, Position.class);
        var t = world.fetchComponent(entity, TextureStorage.class);
        Sprite en = new Sprite(t.getTexture());
        en.setPosition(p.xPos * 64, p.yPos * 64);
        window.draw(en);
    }
}
