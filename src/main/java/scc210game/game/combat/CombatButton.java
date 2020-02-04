package scc210game.game.combat;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UIClickable;
import scc210game.engine.ui.components.UIText;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;

import java.awt.*;
import java.util.Set;
import java.util.function.BiConsumer;

public class CombatButton implements Spawner {

    private float xPosition;
    private float yPosition;
    private float width = 0.1f;
    private float height = 0.1f;

    private final BiConsumer<Entity, World> clickAction;



    public CombatButton(float x, float y, BiConsumer<Entity, World> clickAction)
    {


        this.xPosition = x;
        this.yPosition = y;

        this.clickAction = clickAction;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {

        var position = UiUtils.correctAspectRatio(new Vector2f(this.xPosition, this.yPosition));
        var size = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with(new UITransform(position.x, position.y, 0, size.x, size.y))
                .with(new UIClickable(this.clickAction))
                .with(new Renderable(Set.of(ViewType.MAIN), 2, (Entity e, RenderWindow rw, World w) -> {

                    var dimensions = w.fetchComponent(e, UITransform.class);
                    var color = Color.BLUE;
                    var shape = new RectangleShape(UiUtils.convertUiSize(rw, dimensions.size())){{
                        this.setPosition(UiUtils.convertUiPosition(rw, dimensions.pos()));
                        this.setFillColor(UiUtils.transformColor(color));
                        this.setOutlineColor(UiUtils.transformColor(Color.BLACK));

                    }};

                    rw.draw(shape);

                }));
    }
}
