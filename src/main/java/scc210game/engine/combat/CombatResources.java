package scc210game.engine.combat;

import scc210game.engine.ecs.Component;

public class CombatResources extends Component {

    private boolean playerWeaponRaised = false;
    private boolean enemyWeaponRaised = false;

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
    public String serialize() {
        return null;
    }
}
