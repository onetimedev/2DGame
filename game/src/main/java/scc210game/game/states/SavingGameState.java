package scc210game.game.states;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.state.InputHandlingState;
import scc210game.engine.state.event.KeyDepressedEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransNop;
import scc210game.engine.state.trans.TransPop;
import scc210game.engine.state.trans.Transition;
import scc210game.engine.ui.Font;
import scc210game.engine.utils.UiUtils;
import scc210game.game.resources.SavingGameResource;
import scc210game.game.spawners.ui.BackgroundSpawner;
import scc210game.game.states.events.ToggleSavingGameEvent;

import java.awt.*;
import java.util.Set;

public class SavingGameState extends InputHandlingState {
    static {
        register(SavingGameState.class, j -> new SavingGameState());
    }

    @Override
    public void onStart(World world) {
        world.addResource(new SavingGameResource(world));

        world.entityBuilder()
                .with(new Renderable(Set.of(ViewType.UI), 100, SavingGameState::render))
                .build();

        world.entityBuilder().with(new BackgroundSpawner("pause.png")).build();

        super.onStart(world);
    }

    @Override
    public void onReload(World world) {
        world.addResource(new SavingGameResource(world));
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        var state = world.fetchResource(SavingGameResource.class);

        if (evt instanceof KeyDepressedEvent) {
            KeyDepressedEvent evt1 = (KeyDepressedEvent) evt;
            if (evt1.key == Keyboard.Key.ESCAPE) {
                world.ecs.acceptEvent(new ToggleSavingGameEvent());
                return TransNop.getInstance();
            }
            state.handleInput(evt1.key, world);
        }

        if (evt instanceof ToggleSavingGameEvent) {
            return TransPop.getInstance();
        }

        return super.handleEvent(evt, world);
    }

    private static void drawSave(int idx, String content, RenderWindow rw) {
        var size = UiUtils.correctAspectRatio(new Vector2f(0.2f, 0.05f));
        var pos = UiUtils.correctAspectRatio(new Vector2f(0.2f, 0.25f + (idx * 0.07f)));

        var fillColour = idx == 0 ? new Color(155,66,64) : new Color(145,36,34);

        var rect = new RectangleShape(UiUtils.convertUiSize(rw, size)) {{
            this.setPosition(UiUtils.convertUiPosition(rw, pos));
            this.setFillColor(UiUtils.transformColor(fillColour));
        }};

        rw.draw(rect);

        var text = new Text(content, Font.CaladeaRegular, 50) {{
            this.setPosition(UiUtils.convertUiPosition(rw, pos));
        }};

        rw.draw(text);
    }

    private static void render(Entity e, RenderWindow rw, World w) {
        var state = w.fetchResource(SavingGameResource.class);

        for (SavingGameResource.SaveState save : state.saves) {
            drawSave(save.id - state.selectedSave, String.format("Save: %s", save.id), rw);
        }

        drawSave(state.saves.size() - state.selectedSave, "New Save", rw);
    }
}
