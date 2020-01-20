package scc210game.ui.spawners;

import org.jsfml.graphics.Font;
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
import scc210game.utils.ResourceLoader;
import scc210game.utils.UiUtils;

import java.awt.*;
import java.io.IOException;
import java.util.Set;

public class DialogueSpawner implements Spawner {
    private static Font font = new Font() {{
        try {
            this.loadFromFile(ResourceLoader.resolve("font/FreeSans.ttf"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }};
    private final String message;

    public DialogueSpawner(String message) {
        this.message = message;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        return builder
                .with(new UITransform(0, 0.8f, 0, 1.0f, 0.2f))
                .with(new UIText(this.message))
                .with(new Renderable(Set.of(ViewType.MAIN), 2, (Entity e, RenderWindow rw, World w) -> {
                    var trans = w.fetchComponent(e, UITransform.class);
                    var textContent = w.fetchComponent(e, UIText.class);
                    var rect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
                        setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
                        setFillColor(UiUtils.transformColor(Color.LIGHT_GRAY));
                        setOutlineColor(UiUtils.transformColor(Color.BLACK));
                    }};

                    rw.draw(rect);

                    var text = new Text(textContent.text, font, 24) {{
                        setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
                    }};

                    rw.draw(text);
                }));
    }
}

