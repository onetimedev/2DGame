package scc210game.game.states;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.state.event.KeyPressedEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPop;
import scc210game.engine.state.trans.Transition;
import scc210game.engine.ui.Font;
import scc210game.engine.utils.UiUtils;
import scc210game.game.resources.SavingGameResource;
import scc210game.game.states.events.ToggleSavingGameEvent;

import java.awt.*;
import java.util.Set;

public class SavingGameState extends BaseInGameState {
    @Override
    public void onStart(World world) {
        world.addResource(new SavingGameResource());
        world.entityBuilder()
                .with(new Renderable(Set.of(ViewType.UI), 100, SavingGameState::render))
                .build();
        super.onStart(world);
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        var state = world.fetchResource(SavingGameResource.class);

        if (evt instanceof KeyPressedEvent) {
            KeyPressedEvent evt1 = (KeyPressedEvent) evt;
            state.handleInput(evt1.key, world);
        }

        if (evt instanceof ToggleSavingGameEvent) {
            return TransPop.getInstance();
        }

        return super.handleEvent(evt, world);
    }

    private static void drawSave(SavingGameResource.SaveState s, int idx, String content, RenderWindow rw) {
        var size = new Vector2f(0.6f, 0.05f);
        var pos = new Vector2f(0.2f, 0.5f - (idx * 0.07f));
        var rect = new RectangleShape(UiUtils.convertUiSize(rw, size)) {{
            this.setPosition(UiUtils.convertUiPosition(rw, pos));
            this.setFillColor(UiUtils.transformColor(Color.lightGray));
        }};

        rw.draw(rect);

        var text = new Text(content, Font.freesans, 32) {{
            this.setPosition(UiUtils.convertUiPosition(rw, pos));
        }};

        rw.draw(text);
    }

    private static void render(Entity e, RenderWindow rw, World w) {
        var state = w.fetchResource(SavingGameResource.class);

        for (SavingGameResource.SaveState save : state.saves) {
            drawSave(save, save.id - state.selectedSave, String.format("Save: %s", save.id), rw);
        }
    }
}
