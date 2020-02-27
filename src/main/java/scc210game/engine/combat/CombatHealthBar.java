package scc210game.engine.combat;

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
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatPlayer;
import scc210game.game.components.Health;

import java.awt.*;
import java.util.Set;

public class CombatHealthBar implements Spawner
{

    private float x;
    private float y;
    private float width;
    private float height;

    private boolean enemy;

    public CombatHealthBar(int user)
    {

        if(user == CombatUtils.PLAYER)
        {
            //player
            enemy = false;
            this.x = 0f;
            this.y = 0f;
            this.width = 0.3f;
            this.height = 0.005f;

        }
        else if(user == CombatUtils.BOSS)
        {
            //boss
            enemy = true;
            this.x = 0.7f;
            this.y = 0f;
            this.width = 0.3f;
            this.height = 0.005f;
        }

    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder) {
        var correctedPos = UiUtils.correctAspectRatio(new Vector2f(this.x, this.y));
        var correctedSize = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with(new UITransform(correctedPos.x, correctedPos.y, 0, correctedSize.x, correctedSize.y))
                .with(new Renderable(Set.of(ViewType.UI), 100, (Entity e, RenderWindow rw, World w) -> {
                    var trans = w.fetchComponent(e, UITransform.class);

                    var outerRect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
                        this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
                        this.setFillColor(UiUtils.transformColor(Color.lightGray));
                    }};

                    rw.draw(outerRect);

                    //var health = w.fetchComponent(pl, Health.class);

                    var innerTrans = trans.clone();
                    innerTrans.height *= 0.90;
                    innerTrans.width *= (0.99375 * new CombatUtils().getHealth(w, enemy));
                    innerTrans.updateOrigin(innerTrans.xPos + 0.003125f, innerTrans.yPos + 0.00125f);

                    var innerRect = new RectangleShape(UiUtils.convertUiSize(rw, innerTrans.size())) {{
                        this.setPosition(UiUtils.convertUiPosition(rw, innerTrans.pos()));
                        this.setFillColor(UiUtils.transformColor(getBarColor(w)));
                    }};

                    rw.draw(innerRect);
                }));
    }


    private Color getBarColor(World w)
    {
        float health = new CombatUtils().getAbsHealth(w);

        if(health >= 0 && health <= 10)
        {
            return Color.RED;
        }
        else if(health >= 10 && health <= 20)
        {
            return Color.ORANGE;

        }
        else if(health >= 20 && health <= 50)
        {
            return Color.YELLOW;
        }
        else if(health >= 50 && health <= 100)
        {
            return Color.GREEN;
        }

        return Color.BLACK;
    }
}
