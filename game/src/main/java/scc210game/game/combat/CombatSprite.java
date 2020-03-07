package scc210game.game.combat;

import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class CombatSprite extends Component
{

    public String spriteImage;
    public int enemyState = 0;
    public int playerSprite = 1;
    public boolean signal = false;

    public long nextChange = System.currentTimeMillis();

    public CombatSprite(String spriteImage)
    {
        this.spriteImage = spriteImage;
    }


    @Override
    public Jsonable serialize() {
        return null;
    }
}
