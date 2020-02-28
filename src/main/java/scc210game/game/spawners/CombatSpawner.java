package scc210game.game.spawners;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import scc210game.engine.combat.SpriteType;
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
    private SpriteType spriteInfo;

    private Sprite image;



    public CombatSpawner(SpriteType spriteInfo)
    {
        this.spriteInfo = spriteInfo;
        setY();
        if(!this.spriteInfo.getEnemyStatus())
        {
            xPosition = 0.0f;
            yPosition = (0.64f - this.height);
        }
        else
        {
            xPosition = 0.75f;
            yPosition = 0.2f;
            System.out.println("Image scale: " + this.image.getTexture().getSize().y);
            //yPosition = (1f - this.image.getScale().y);
        }

    }

    private void setY()
    {
        Texture t = new Texture();
        try {
            //String spriteImage = this.enemy ? "./src/main/resources/textures/boss_water.png" : "./src/main/resources/textures/player_anim.png";
            t.loadFromFile(Paths.get(this.spriteInfo.getTextureLocation()));
            this.image = new Sprite(t);

            if(!this.spriteInfo.getEnemyStatus())
            {
                this.image.setScale(new Vector2f(5, 5));
            }
            else
            {
                this.image.setScale(new Vector2f(4,4));
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {

        var position = UiUtils.correctAspectRatio(new Vector2f(this.xPosition, this.yPosition));
        var size = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with((this.spriteInfo.getEnemyStatus() ? new CombatEnemy() : new CombatPlayer()))
                .with(new UITransform(position.x, position.y, 1, size.x, size.y))
                .with(new Renderable(Set.of(ViewType.UI), 2,
                        (Entity e, RenderWindow rw, World w) -> {
                            var dimensions = w.fetchComponent(e, UITransform.class);
                            if(!this.spriteInfo.getEnemyStatus()) this.image.setTextureRect(new IntRect(0, 0, 64, 64));
                            this.image.setPosition(UiUtils.convertUiPosition(rw, dimensions.pos()));
                            rw.draw(this.image);

                }));
    }
}