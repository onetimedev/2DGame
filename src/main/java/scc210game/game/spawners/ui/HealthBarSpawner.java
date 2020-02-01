package scc210game.game.spawners.ui;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.Health;

import java.awt.*;
import java.util.Set;

public class HealthBarSpawner implements Spawner {
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final Entity player;

    public HealthBarSpawner(float x, float y, float width, float height, Entity player) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.player = player;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        var correctedPos = UiUtils.correctAspectRatio(new Vector2f(this.x, this.y));
        var correctedSize = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with(new UITransform(correctedPos.x, correctedPos.y, 0, correctedSize.x, correctedSize.y))
                .with(new Renderable(Set.of(ViewType.MAIN), 2, (Entity e, RenderWindow rw, World w) -> {
                    var trans = w.fetchComponent(e, UITransform.class);

                    var outerRect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
                        this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
                        this.setFillColor(UiUtils.transformColor(Color.lightGray));
                    }};

                    rw.draw(outerRect);

                    var health = w.fetchComponent(player, Health.class);

                    var innerTrans = trans.clone();
                    innerTrans.height *= 0.90;
                    innerTrans.width *= (0.99375 * health.hpPercent());
                    innerTrans.updateOrigin(innerTrans.xPos + 0.003125f, innerTrans.yPos + 0.00125f);

                    var innerRect = new RectangleShape(UiUtils.convertUiSize(rw, innerTrans.size())) {{
                        this.setPosition(UiUtils.convertUiPosition(rw, innerTrans.pos()));
                        this.setFillColor(UiUtils.transformColor(Color.red));
                    }};

                    rw.draw(innerRect);
                }));
    }
}
