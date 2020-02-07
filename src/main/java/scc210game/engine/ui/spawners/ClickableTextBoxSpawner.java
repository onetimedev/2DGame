package scc210game.engine.ui.spawners;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.Font;
import scc210game.engine.ui.components.UIClickable;
import scc210game.engine.ui.components.UIHovered;
import scc210game.engine.ui.components.UIText;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;

import java.awt.*;
import java.util.Set;
import java.util.function.BiConsumer;

public class ClickableTextBoxSpawner implements Spawner {
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final String text;
    private final BiConsumer<Entity, World> clickAction;

    public ClickableTextBoxSpawner(float x, float y, float width, float height, String text, BiConsumer<Entity, World> clickAction) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.clickAction = clickAction;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        var correctedPos = UiUtils.correctAspectRatio(new Vector2f(this.x, this.y));
        var correctedSize = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with(new UIText(this.text))
                .with(new UITransform(correctedPos.x, correctedPos.y, 0, correctedSize.x, correctedSize.y))
                .with(new UIClickable(this.clickAction))
                .with(new Renderable(Set.of(ViewType.UI), 2, (Entity e, RenderWindow rw, World w) -> {
                    var trans = w.fetchComponent(e, UITransform.class);
                    var textContent = w.fetchComponent(e, UIText.class);

                    var fillColour = w.hasComponent(e, UIHovered.class) ? Color.blue : Color.lightGray;

                    var rect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
                        this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
                        this.setFillColor(UiUtils.transformColor(fillColour));
                        this.setOutlineColor(UiUtils.transformColor(Color.BLACK));
                    }};

                    rw.draw(rect);

                    var text = new Text(textContent.text, Font.freesans, 24) {{
                        this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
                    }};

                    rw.draw(text);
                }));
    }
}
