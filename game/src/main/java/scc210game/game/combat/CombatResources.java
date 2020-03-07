package scc210game.game.combat;

import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Resource;

public class CombatResources extends Resource {

    private boolean playerWeaponRaised = false;
    private boolean enemyWeaponRaised = false;

    public boolean isCombatActive = false;
    public boolean activeAnimation = true;

    public void raisePlayerWeapon()
    {
        this.playerWeaponRaised = true;
    }

    public void lowerPlayerWeapon()
    {
        this.playerWeaponRaised = false;
    }


    public void raiseEnemyWeapon()
    {
        this.enemyWeaponRaised = true;
    }

    public void lowerEnemyWeapon()
    {
        this.enemyWeaponRaised = false;
    }


    public boolean getPlayerWeaponRaised()
    {
        return this.playerWeaponRaised;
    }

    public boolean getEnemyWeaponRaised()
    {
        return this.playerWeaponRaised;
    }

    @Override
    public Jsonable serialize() {
        return null;
    }
}
