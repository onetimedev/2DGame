package scc210game.game.spawners;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.*;
import scc210game.engine.ecs.Component;
import scc210game.engine.movement.Position;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatPlayer;
import scc210game.game.map.Player;

import java.awt.*;
import java.io.IOException;
import java.lang.System;
import java.nio.file.Paths;
import java.util.Set;

public class CombatSpawner implements Spawner {

    private float xPosition;
    private float yPosition;
    private float width = 0.2f;
    private float height = 0.3f;


    private boolean enemy;



    public CombatSpawner(boolean enemy)
    {

        if(!enemy)
        {
            xPosition = 0.0f;
            yPosition = 0.33f;
        }
        else
        {
            xPosition = 0.75f;
            yPosition = 0.05f;
        }

        this.enemy = enemy;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {

        var position = UiUtils.correctAspectRatio(new Vector2f(this.xPosition, this.yPosition));
        var size = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with((this.enemy ? new CombatEnemy() : new CombatPlayer()))
                .with(new UITransform(position.x, position.y, 1, size.x, size.y))
                .with(new Renderable(Set.of(ViewType.MAIN), 2,
                        (Entity e, RenderWindow rw, World w) -> {

                            var dimensions = w.fetchComponent(e, UITransform.class);
                            Texture t = new Texture();
                            try {
                                String spriteImage = this.enemy ? "./src/main/resources/textures/boss_final.png" : "./src/main/resources/textures/player.png";
                                t.loadFromFile(Paths.get(spriteImage));

                                Sprite pl = new Sprite(t);



                                pl.setPosition(UiUtils.convertUiPosition(rw, dimensions.pos()));
                                //pl.setTextureRect(new IntRect(0,0,200,200));

                                if(!this.enemy)
                                {
                                    pl.setScale(new Vector2f(4, 4));
                                }
                                else
                                {
                                    pl.setScale(new Vector2f(3,3));
                                }

                                rw.draw(pl);

                            }
                            catch (IOException error) {
                                System.out.println("error");
                                throw new RuntimeException();
                            }

                }));
    }
}