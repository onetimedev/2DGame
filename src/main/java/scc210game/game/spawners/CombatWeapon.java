package scc210game.game.spawners;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatEnemyWeapon;
import scc210game.game.components.CombatPlayer;
import scc210game.game.components.CombatPlayerWeapon;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class CombatWeapon implements Spawner {

    private float xPosition;
    private float yPosition;
    private float width = 0.05f;
    private float height = 0.1f;
    private float rotation = -50f;//starting rotation


    private boolean enemy;



    public CombatWeapon(boolean enemy, World world)
    {
        this.enemy = enemy;
        if(!enemy)
        {
            var combatPlayerSprite = world.applyQuery(Query.builder().require(CombatPlayer.class).build()).findFirst().get();
            var cplayerPosition = world.fetchComponent(combatPlayerSprite, UITransform.class);
            this.xPosition = (cplayerPosition.width)-0.05f;
            this.yPosition = (cplayerPosition.height/2)+0.2f;

        }
        else
        {
            var combatEnemySprite = world.applyQuery(Query.builder().require(CombatEnemy.class).build()).findFirst().get();
            var combatplayerPosition = world.fetchComponent(combatEnemySprite, UITransform.class);
            this.xPosition = combatplayerPosition.xPos;
            this.yPosition = (combatplayerPosition.height/2)+0.2f;
        }

    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {

        var position = UiUtils.correctAspectRatio(new Vector2f(this.xPosition, this.yPosition));
        var size = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with((this.enemy ? new CombatEnemyWeapon() : new CombatPlayerWeapon()))
                .with(new UITransform(position.x, position.y, 1, size.x, size.y, rotation))
                .with(new Renderable(Set.of(ViewType.MAIN), 2,
                        (Entity e, RenderWindow rw, World w) -> {

                    var dimensions = w.fetchComponent(e, UITransform.class);
                    Texture t = new Texture();
                    try {
                        t.loadFromFile(Paths.get("./src/main/resources/textures/sword.png"));
                        Sprite pl = new Sprite(t);


                        pl.setPosition(UiUtils.convertUiPosition(rw, dimensions.pos()));
                        //pl.setTextureRect(new IntRect(0,0,200,200));
                        pl.setRotation(dimensions.rotation);
                        pl.setOrigin(0, 0);
                        rw.draw(pl);

                    }
                    catch (IOException error) {
                        System.out.println("error");
                        throw new RuntimeException();
                    }

                    /*
                    var dimensions = w.fetchComponent(e, UITransform.class);
                    var color = Color.YELLOW;
                    var shape = new RectangleShape(UiUtils.convertUiSize(rw, dimensions.size())){{
                        this.setPosition(UiUtils.convertUiPosition(rw, dimensions.pos()));
                        this.setFillColor(UiUtils.transformColor(color));
                        this.setOutlineColor(UiUtils.transformColor(Color.BLACK));
                        this.setRotation(dimensions.rotation);

                    }};

                    rw.draw(shape);

                     */

                }));
    }
}