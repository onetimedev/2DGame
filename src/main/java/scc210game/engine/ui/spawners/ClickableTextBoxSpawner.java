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
import scc210game.engine.utils.SerializableBiConsumer;
import scc210game.engine.utils.UiUtils;

import java.awt.*;
import java.util.Set;

public class ClickableTextBoxSpawner implements Spawner {
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final String text;
    private final SerializableBiConsumer<Entity, World> clickAction;

    public ClickableTextBoxSpawner(float x, float y, float width, float height, String text, SerializableBiConsumer<Entity, World> clickAction) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.clickAction = clickAction;
    }


    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        var correctedPos = UiUtils.correctAspectRatio(new Vector2f(this.x, this.y));
        var correctedSize = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with(new UIText(this.text))
                .with(new UITransform(correctedPos.x, correctedPos.y, 0, correctedSize.x, correctedSize.y))
                .with(new UIClickable(this.clickAction))
                .with(new Renderable(Set.of(ViewType.UI), 100, ClickableTextBoxSpawner::accept));
    }


  /**
   * Creating and rendering the boxes in the window
   * @param e the entity to fetch components for
   * @param rw the render window
   * @param w the world for the current state
   */
  private static void accept(Entity e, RenderWindow rw, World w) {
    var trans = w.fetchComponent(e, UITransform.class);
    var textContent = w.fetchComponent(e, UIText.class);

    var fillColour = w.hasComponent(e, UIHovered.class) ? Color.GRAY : Color.lightGray;

    var rect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
      this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
      this.setFillColor(UiUtils.transformColor(fillColour));
      this.setOutlineColor(UiUtils.transformColor(Color.BLACK));
    }};

    rw.draw(rect);

    var text = new Text(textContent.text, Font.CaladeaRegular, 50) {{
      this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
    }};

    rw.draw(text);
  }
}
