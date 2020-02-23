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
import scc210game.game.map.NPC;
import scc210game.game.map.Tile;
import scc210game.game.utils.MapHelper;

import java.util.Set;

public class NPCSpawner implements Spawner {
    private final Tile npcTile;
    private final int xSpawn;
    private final int ySpawn;
    private Texture npcTexture = new Texture();


    public NPCSpawner(Tile t) {
        this.npcTile = t;
        this.xSpawn = this.npcTile.getXPos();
        this.ySpawn = this.npcTile.getYPos();
        this.setTexture(t.getTextureName());
    }


    public void setTexture(String type) {
        switch (type) {
            case "story.png": {
                this.npcTile.setHasCollision(true);
                this.npcTile.setCanHaveStory(true);
                this.npcTexture = MapHelper.loadTexture("story.png");
                break;
            }
        }
    }


    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        return builder
                .with(new NPC())
                .with(new Position(this.xSpawn, this.ySpawn))
                .with(new Renderable(Set.of(ViewType.MAIN), 5,
                        (Entity e, RenderWindow rw, World w) -> {

                            Sprite npc = new Sprite(this.npcTexture);
                            npc.setPosition(this.xSpawn * 64, this.ySpawn * 64);

                            rw.draw(npc);
                        }));
    }

    }
