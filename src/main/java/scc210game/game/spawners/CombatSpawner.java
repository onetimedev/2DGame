package scc210game.game.spawners;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import scc210game.engine.combat.CombatImage;
import scc210game.engine.combat.CombatSprite;
import scc210game.engine.combat.SpriteType;
import scc210game.engine.ecs.*;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatPlayer;

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
    Texture t = new Texture();

    Class<? extends Component> klass;

    public CombatSpawner(SpriteType spriteInfo)
    {
        this.spriteInfo = spriteInfo;
        if(!this.spriteInfo.getEnemyStatus())
        {
            xPosition = 0.0f;
            yPosition = (0.64f - this.height);
            this.klass = CombatPlayer.class;
        }
        else
        {
            xPosition = 0.75f;
            yPosition = 0.25f;
            this.klass = CombatEnemy.class;
        }

    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {

        var position = UiUtils.correctAspectRatio(new Vector2f(this.xPosition, this.yPosition));
        var size = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));
        return builder
                .with((this.spriteInfo.getEnemyStatus() ? new CombatEnemy() : new CombatPlayer()))
                .with(new UITransform(position.x, position.y, 1, size.x, size.y))
                .with(new CombatImage(spriteInfo.getTextureLocation()))
                .with(new Renderable(Set.of(ViewType.UI), 2,
                        (Entity e, RenderWindow rw, World w) -> {

                            var playerEnt = w.applyQuery(Query.builder().require(klass).build()).findFirst().orElseThrow();
                            var image = w.fetchComponent(playerEnt, CombatImage.class);

                            Texture t = new Texture();
                            try {
                                //String spriteImage = this.enemy ? "./src/main/resources/textures/boss_water.png" : "./src/main/resources/textures/player_anim.png";
                                t.loadFromFile(Paths.get(image.getPath()));
                                if(!this.spriteInfo.getEnemyStatus())
                                {
                                    this.image = new Sprite(t);
                                    this.image.setScale(new Vector2f(5.61f, 5.61f));
                                }
                                else
                                {
                                    var spriteState = w.applyQuery(Query.builder().require(CombatSprite.class).build()).findFirst().orElseThrow();
                                    var state = w.fetchComponent(spriteState, CombatSprite.class);
                                    if(System.currentTimeMillis() >= state.nextChange && state.signal){
                                        this.image = new Sprite(t, new IntRect(365*state.state,0,365,365));
                                        if(state.state < 3) {
                                            state.nextChange = System.currentTimeMillis() + 60;
                                            state.state++;
                                        }else{
                                            state.state = 3;
                                            this.image = new Sprite(t, new IntRect(365*state.state,0,365,365));
                                            state.signal = false;
                                        }
                                    }else{
                                        this.image = new Sprite(t, new IntRect(365*state.state,0,365,365));
                                    }
                                    //System.out.println(this.image.getGlobalBounds().height);
                                    this.image.setScale(new Vector2f(1.3f,1.3f));

                                }

                            }
                            catch (IOException error)
                            {
                                error.printStackTrace();
                            }
                            var dimensions = w.fetchComponent(e, UITransform.class);
                            if(!this.spriteInfo.getEnemyStatus()) {
                                this.image.setPosition(position.x*64, position.y*64);
                                //this.image.setTextureRect(new IntRect(0, 0, 64, 64));
                            }




                            this.image.setPosition(UiUtils.convertUiPosition(rw, dimensions.pos()));
                            rw.draw(this.image);

                }));
    }

}