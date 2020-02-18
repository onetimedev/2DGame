package scc210game.engine.ui.spawners;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.Font;
import scc210game.engine.ui.components.UIHovered;
import scc210game.engine.ui.components.UIText;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;

import java.awt.*;
import java.util.Set;

public class DialogueSpawner implements Spawner {
    private final String message;

    public DialogueSpawner(String message) {
        this.message = message;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        return builder
                .with(new UITransform(0, 0.8f, 0, 1.0f, 0.2f))
                .with(new UIText(this.message))
                .with(new Renderable(Set.of(ViewType.UI), 2, (Entity e, RenderWindow rw, World w) -> {
                    var trans = w.fetchComponent(e, UITransform.class);
                    var textContent = w.fetchComponent(e, UIText.class);

                    var fillColour = w.hasComponent(e, UIHovered.class) ? Color.RED : Color.LIGHT_GRAY;

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

