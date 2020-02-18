package scc210game.engine.animation;

import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.ui.components.UITransform;
import scc210game.game.components.CombatEnemy;

public class CombatUtils
{
    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int ROTATE = 2;

    public static final float X_AXIS_MOVE_DISTANCE = 0.005f;

    public boolean hasCollided(UITransform rect1, UITransform rect2)
    {

        if (rect1.xPos < rect2.xPos + rect2.width &&
                rect1.xPos + rect1.width > rect2.xPos &&
                rect1.yPos < rect2.yPos + rect2.height &&
                rect1.yPos + rect1.height > rect2.yPos) {
            return true;
        }

        return false;

    }

    public UITransform getEnemy(World w){
        var combatPlayer = w.applyQuery(Query.builder().require(CombatEnemy.class).build()).findFirst().get();
        return w.fetchComponent(combatPlayer, UITransform.class);
    }

}