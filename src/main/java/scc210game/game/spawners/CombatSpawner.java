package scc210game.game.spawners;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import scc210game.engine.combat.CombatImage;
import scc210game.engine.combat.CombatSprite;
import scc210game.engine.combat.CombatUtils;
import scc210game.engine.combat.SpriteType;
import scc210game.engine.ecs.*;
import scc210game.engine.render.MainWindowResource;
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

    World world;

    public CombatSpawner(SpriteType spriteInfo, World world)
    {
        this.world = world;

        try{
            t.loadFromFile(Paths.get(spriteInfo.getTextureLocation()));
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        this.spriteInfo = spriteInfo;
        if(!this.spriteInfo.getEnemyStatus())
        {
            xPosition = 0.0f;
            //yPosition = (0.63f - this.height);
            this.klass = CombatPlayer.class;
        }
        else
        {
            float textureHeight = (float) t.getSize().y;
            xPosition = 0.75f;
            this.klass = CombatEnemy.class;
        }

        yPosition = 0.35f;


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
                            var spriteState = w.applyQuery(Query.builder().require(CombatSprite.class).build()).findFirst().orElseThrow();
                            var state = w.fetchComponent(spriteState, CombatSprite.class);
                                //String spriteImage = this.enemy ? "./src/main/resources/textures/boss_water.png" : "./src/main/resources/textures/player_anim.png";



                                if(!this.spriteInfo.getEnemyStatus())
                                {
                                    this.image = new Sprite(t, new IntRect(365*state.playerSprite,0,365,365));
                                    //this.image.setScale(new Vector2f(5.61f, 5.61f));
                                }
                                else
                                {
                                    if(System.currentTimeMillis() >= state.nextChange && state.signal){
                                        this.image = new Sprite(t, new IntRect(365*state.enemyState,0,365,365));
                                        System.out.println(t.getSize().x);
                                        int maxFrame = (t.getSize().x / t.getSize().y)-1;
                                        if(state.enemyState < maxFrame) {
                                            state.nextChange = System.currentTimeMillis() + 60;
                                            state.enemyState++;
                                        }else{
                                            state.enemyState = maxFrame;
                                            this.image = new Sprite(t, new IntRect(365*state.enemyState,0,365,365));
                                            state.signal = false;
                                        }
                                    }else{
                                        this.image = new Sprite(t, new IntRect(365*state.enemyState,0,365,365));
                                    }
                                    //System.out.println(this.image.getGlobalBounds().height);


                                    if(spriteInfo.getEnemyLevel() == CombatUtils.ENEMY_DAMAGE)
                                    {
                                        this.image.setScale(new Vector2f(1f,1f));
                                    }
                                    else if(spriteInfo.getEnemyLevel() == CombatUtils.BOSS_DAMAGE)
                                    {
                                        this.image.setScale(new Vector2f(1.3f, 1.3f));
                                        yPosition -= 0.005f;
                                    }
                                    else if(spriteInfo.getEnemyLevel() == CombatUtils.FINAL_BOSS_DAMAGE) {
                                        this.image.setScale(new Vector2f(1.5f, 1.5f));
                                        System.out.println("In final boss");
                                    }


                                }

                            var dimensions = w.fetchComponent(e, UITransform.class);




                            this.image.setPosition(UiUtils.convertUiPosition(rw, dimensions.pos()));
                            rw.draw(this.image);

                }));
    }

}