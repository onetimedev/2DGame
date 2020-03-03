package scc210game.game.spawners;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
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
    private float rotation = 0f;//starting rotation


    private boolean enemy;


    public int damage;
    public String imageLocation;


    public Texture t = new Texture();

    public CombatWeapon(boolean enemy, World world, int damage, String location)
    {
        this.enemy = enemy;
        var combatPlayerSprite = world.applyQuery(Query.builder().require(CombatPlayer.class).build()).findFirst().get();
        var cplayerPosition = world.fetchComponent(combatPlayerSprite, UITransform.class);
        this.xPosition = (cplayerPosition.width)-0.0f;
        this.yPosition = (cplayerPosition.height/2)+0.23f;

        this.damage = damage;
        this.imageLocation = location;

        try {
            t.loadFromFile(Paths.get(this.imageLocation));
        }catch (IOException e)
        {
            e.printStackTrace();
        }



    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {

        var position = UiUtils.correctAspectRatio(new Vector2f(this.xPosition, this.yPosition));
        var size = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with(new CombatPlayerWeapon(damage, imageLocation))
                .with(new UITransform(position.x, position.y, 2, size.x, size.y, rotation))
                .with(new Renderable(Set.of(ViewType.UI), 2,
                        (Entity e, RenderWindow rw, World w) -> {

                    var dimensions = w.fetchComponent(e, UITransform.class);
                        Sprite pl = new Sprite(t);

                        pl.setPosition(UiUtils.convertUiPosition(rw, dimensions.pos()));
                        pl.setScale(new Vector2f(1,1));
                        //pl.setTextureRect(new IntRect(0,0,200,200));
                        pl.setOrigin((xPosition/2)+30,(yPosition/2)+30);
                        pl.setRotation(dimensions.rotation);
                        rw.draw(pl);


                }));
    }
}