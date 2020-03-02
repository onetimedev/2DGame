package scc210game.engine.combat;

import scc210game.engine.ecs.Component;

public class CombatSprite extends Component
{

    public String spriteImage;
    public int state = 0;
    public boolean signal = false;

    public long nextChange = System.currentTimeMillis();

    public CombatSprite(String spriteImage)
    {
        this.spriteImage = spriteImage;
    }


    @Override
    public String serialize() {
        return null;
    }
}
