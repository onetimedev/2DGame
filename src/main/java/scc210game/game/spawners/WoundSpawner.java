package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import scc210game.engine.combat.CombatUtils;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.ResourceLoader;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatPlayerWeapon;
import scc210game.game.components.TargetPosition;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Set;

public class WoundSpawner implements Spawner {

    private float xPosition;
    private float yPosition;
    private float width = 0.01f;
    private float height = 0.01f;

    private Texture t = new Texture();
    public WoundSpawner()
    {

        try {
            t.loadFromStream(ResourceLoader.resolve(CombatUtils.TARGET_TEXTURE));
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {

        var targetEntity = world.applyQuery(Query.builder().require(TargetPosition.class).build()).findFirst().get();
        var target =  world.fetchComponent(targetEntity, TargetPosition.class);

        var enemyEntity = world.applyQuery(Query.builder().require(CombatEnemy.class).build()).findFirst().get();


        return builder
                .with()
                .with(new Renderable(Set.of(ViewType.UI), 4,
                        (Entity e, RenderWindow rw, World w) -> {

                            var dimensions = w.fetchComponent(enemyEntity, UITransform.class);
                            Sprite pl = new Sprite(t);

                            float newX = dimensions.xPos + 0.1f + target.offset;
                            float newY = dimensions.yPos + 0.1f + target.offset;


                            UITransform newAttr = new UITransform(dimensions);
                            newAttr.xPos = newX;
                            newAttr.yPos = newY;

                            pl.setPosition(UiUtils.convertUiPosition(rw, newAttr.pos()));
                            //pl.setScale(new Vector2f(2.25f,2.25f));
                            //pl.setTextureRect(new IntRect(0,0,200,200));
                            //pl.setOrigin((xPosition/2)+30,(yPosition/2)+30);
                            //pl.setRotation(dimensions.rotation);

                            if(target.visible)
                            {
                                if(System.currentTimeMillis() < target.visibleUntil)
                                {
                                    rw.draw(pl);
                                }else{
                                    target.visible = false;
                                }
                            }
                }));
    }



}
