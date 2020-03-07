package scc210game.game.spawners;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UIDraggable;
import scc210game.engine.ui.components.UIHovered;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;

import java.awt.*;
import java.util.Set;

public class DraggableBoxSpawner implements Spawner {
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public DraggableBoxSpawner(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        var correctedPos = UiUtils.correctAspectRatio(new Vector2f(this.x, this.y));
        var correctedSize = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with(new UITransform(correctedPos.x, correctedPos.y, 0, correctedSize.x, correctedSize.y))
                .with(new UIDraggable())
                .with(new Renderable(Set.of(ViewType.UI), 100, (Entity e, RenderWindow rw, World w) -> {
                    var trans = w.fetchComponent(e, UITransform.class);

                    var fillColour = w.hasComponent(e, UIHovered.class) ? Color.GREEN : new Color(0.0f, 0.2f, 0.1f, 0.5f);

                    var rect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
                        this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
                        this.setFillColor(UiUtils.transformColor(fillColour));
                        this.setOutlineColor(UiUtils.transformColor(Color.BLACK));
                    }};

                    rw.draw(rect);
                }));
    }
}
