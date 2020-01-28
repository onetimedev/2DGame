package scc210game.ui.spawners;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import scc210game.ecs.Entity;
import scc210game.ecs.Spawner;
import scc210game.ecs.World;
import scc210game.render.Renderable;
import scc210game.render.ViewType;
import scc210game.ui.components.UIDroppable;
import scc210game.ui.components.UIHovered;
import scc210game.ui.components.UITransform;
import scc210game.utils.UiUtils;

import java.awt.*;
import java.util.Set;

public class DroppableBoxSpawner implements Spawner {
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public DroppableBoxSpawner(float x, float y, float width, float height) {
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
                .with(new UIDroppable((Entity thisEntity, Entity droppedEntity, World w) -> {
                    var thisTrans = w.fetchComponent(thisEntity, UITransform.class);
                    var droppedTrans = w.fetchComponent(droppedEntity, UITransform.class);

                    var centerdPosition = UiUtils.centerTransforms(droppedTrans.size(), thisTrans.pos(), thisTrans.size());

                    droppedTrans.updateOrigin(centerdPosition.x, centerdPosition.y);
                }))
                .with(new Renderable(Set.of(ViewType.MAIN), 2, (Entity e, RenderWindow rw, World w) -> {
                    var trans = w.fetchComponent(e, UITransform.class);

                    var fillColour = w.hasComponent(e, UIHovered.class) ? Color.RED : Color.LIGHT_GRAY;

                    var rect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
                        this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
                        this.setFillColor(UiUtils.transformColor(fillColour));
                        this.setOutlineColor(UiUtils.transformColor(Color.BLACK));
                    }};

                    rw.draw(rect);
                }));
    }
}
