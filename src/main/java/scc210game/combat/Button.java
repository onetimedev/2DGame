package scc210game.combat;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import scc210game.ecs.Entity;
import scc210game.ecs.Query;
import scc210game.ecs.Spawner;
import scc210game.ecs.World;
import scc210game.render.Renderable;
import scc210game.render.ViewType;

import scc210game.ui.UITransform;
import scc210game.utils.UiUtils;

import java.awt.*;
import java.util.Set;

public class Button implements Spawner{
    private final float x;
    private final float y;
    private final float width;
    private final float height;


    public Button(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        var correctedPos = UiUtils.correctAspectRatio(new Vector2f(this.x, this.y));
        var correctedSize = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));



        return builder

                .with(new UITransform(correctedPos.x, correctedPos.y, 0, correctedSize.x, correctedSize.y))
                .with(new UIClickable((Entity thisEntity, World w) -> {

                    var cEn = w.applyQuery(Query.builder().require(Player.class).build()).findFirst().get();

                    var trans = w.fetchComponent(cEn, UITransform.class);
                    //trans.xPos += 0.01;

                    float xP = trans.xPos;

                    xP += 0.03;
                    trans.xPos = xP;





                }))
                .with(new Renderable(Set.of(ViewType.MAIN), 2, (Entity e, RenderWindow rw, World w) -> {
                    var trans = w.fetchComponent(e, UITransform.class);

                    var fillColour = w.hasComponent(e, UIHovered.class) ? Color.gray : Color.lightGray;

                    var rect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
                        this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
                        this.setFillColor(UiUtils.transformColor(fillColour));
                        this.setOutlineColor(UiUtils.transformColor(Color.BLACK));
                    }};

                    rw.draw(rect);
                }));
    }




    private boolean hasCollided(UITransform rect1, UITransform rect2)
    {

        if (rect1.xPos < rect2.xPos + rect2.width &&
                rect1.xPos + rect1.width > rect2.xPos &&
                rect1.yPos < rect2.yPos + rect2.height &&
                rect1.yPos + rect1.height > rect2.yPos) {

        }

        return false;

    }


}
