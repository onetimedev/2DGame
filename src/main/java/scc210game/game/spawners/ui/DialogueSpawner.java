package scc210game.game.spawners.ui;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.Font;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.SerializableBiConsumer;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.Dialogue;

import java.awt.*;
import java.util.Set;

public class DialogueSpawner implements Spawner {
    private final String message;
    private final SerializableBiConsumer<Entity, World> accept;
    private final SerializableBiConsumer<Entity, World> ignore;

    public DialogueSpawner(String message, SerializableBiConsumer<Entity, World> accept, SerializableBiConsumer<Entity, World> ignore) {
        this.message = message;
        this.accept = accept;
        this.ignore = ignore;
    }

    private static void accept(Entity e, RenderWindow rw, World w) {
        var trans = w.fetchComponent(e, UITransform.class);
        var dialogue = w.fetchComponent(e, Dialogue.class);

        var fillColour = new Color(0.2f, 0.2f, 0.2f, 0.8f);

        var rect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
            this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
            this.setFillColor(UiUtils.transformColor(fillColour));
            this.setOutlineColor(UiUtils.transformColor(Color.BLACK));
        }};

        rw.draw(rect);

        var text = new Text(dialogue.text, Font.freesans, 24) {{
            this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
        }};

        rw.draw(text);
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        return builder
                .with(new Dialogue(this.message, this.accept, this.ignore))
                .with(new UITransform(0, 0.8f, 0, 1.0f, 0.2f))
                .with(new Renderable(Set.of(ViewType.UI), 100, DialogueSpawner::accept));
    }
}

