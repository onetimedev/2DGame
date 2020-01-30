package scc210game.combat;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import scc210game.ecs.Entity;
import scc210game.ecs.Spawner;
import scc210game.ecs.World;
import scc210game.render.Renderable;
import scc210game.render.ViewType;
import scc210game.ui.UIText;
import scc210game.ui.UITransform;
import scc210game.utils.UiUtils;

import java.awt.*;
import java.util.Set;

public class CombatUI implements Spawner {

    private float xPos;
    private float yPos;
    private int zPos;
    private float width;
    private float height;

    public CombatUI(float xPos, float yPos, int zPos, float width, float height)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.width = width;
        this.height = height;
    }


    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        return builder
                .with(new UITransform(this.xPos, this.yPos, this.zPos, this.width, this.height))
                .with(new Renderable(Set.of(ViewType.MAIN), 2, (Entity e, RenderWindow rw, World w) -> {


                    var trans = w.fetchComponent(e, UITransform.class);

                    var rect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
                        this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));

                        this.setFillColor(UiUtils.transformColor(java.awt.Color.GREEN));

                        this.setOutlineColor(UiUtils.transformColor(Color.GRAY));
                    }};

                    rw.draw(rect);

                })
                );

    }

}
