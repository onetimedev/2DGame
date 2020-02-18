package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.movement.Position;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.utils.MapHelper;
import scc210game.game.map.NPC;
import scc210game.game.map.Tile;

import java.util.Set;

public class NPCSpawner implements Spawner {
    private Tile npcTile;
    private int xSpawn;
    private int ySpawn;
    private Texture npcTexture = new Texture();


    public NPCSpawner(Tile t) {
        npcTile = t;
        xSpawn = npcTile.getXPos();
        ySpawn = npcTile.getYPos();
        setTexture(t.getTextureName());
    }


    public void setTexture(String type) {
        switch (type) {
            case "story.png": {
                npcTile.setHasCollision(true);
                npcTile.setCanHaveStory(true);
                npcTexture = MapHelper.loadTexture("story.png");
                break;
            }
        }
    }


    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        return builder
                .with(new NPC())
                .with(new Position(xSpawn, ySpawn))
                .with(new Renderable(Set.of(ViewType.MAIN), 5,
                        (Entity e, RenderWindow rw, World w) -> {

                            Sprite npc = new Sprite(npcTexture);
                            npc.setPosition(xSpawn * 64, ySpawn * 64);

                            rw.draw(npc);
                        }));
    }

    }
