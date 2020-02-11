package scc210game.game.spawners;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatPlayer;

import java.awt.*;
import java.util.Set;

public class CombatSpawner implements Spawner {

    private float xPosition;
    private float yPosition = 0.25f;
    private float width = 0.2f;
    private float height = 0.3f;


    private boolean enemy;



    public CombatSpawner(boolean enemy)
    {

        if(!enemy)
        {
            xPosition = 0.0f;
        }
        else
        {
            xPosition = 0.8f;
        }

        this.enemy = enemy;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {

        var position = UiUtils.correctAspectRatio(new Vector2f(this.xPosition, this.yPosition));
        var size = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with((this.enemy ? new CombatEnemy() : new CombatPlayer()))
                .with(new UITransform(position.x, position.y, 0, size.x, size.y))
                .with(new Renderable(Set.of(ViewType.MAIN), 2, (Entity e, RenderWindow rw, World w) -> {

                    var dimensions = w.fetchComponent(e, UITransform.class);
                    var color = Color.BLUE;
                    var shape = new RectangleShape(UiUtils.convertUiSize(rw, dimensions.size())){{
                        this.setPosition(UiUtils.convertUiPosition(rw, dimensions.pos()));
                        this.setFillColor(UiUtils.transformColor(color));
                        this.setOutlineColor(UiUtils.transformColor(Color.BLACK));

                    }};

                    rw.draw(shape);

                }));
    }
}