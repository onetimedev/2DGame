package scc210game.game.spawners.ui;

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
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;
import scc210game.game.spawners.WeaponSpawner;
import scc210game.game.states.events.EnterInventoryEvent;

import java.awt.*;
import java.util.Set;

public class EnterInventoryButtonSpawner implements Spawner {
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public EnterInventoryButtonSpawner(float x, float y, float width, float height) {
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
                .with(new UIText("Inventory"))
                .with(new UITransform(correctedPos.x, correctedPos.y, 0, correctedSize.x, correctedSize.y))
                .with(new UIClickable((entity, world) -> {
                    var inv = new Inventory(14);
                    var invEnt = world.entityBuilder()
                            .with(inv)
                            .build();

                    for (var i = 0; i < 4; i++) {
                        var itemEnt = world.entityBuilder()
                                .with(new WeaponSpawner(i * 10))
                                .build();
                        var item = world.fetchComponent(itemEnt, Item.class);
                        inv.addItem(itemEnt, item);
                    }

                    world.ecs.acceptEvent(new EnterInventoryEvent(inv));
                }))
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
