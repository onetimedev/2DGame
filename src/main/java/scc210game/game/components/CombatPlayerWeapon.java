package scc210game.game.components;

import scc210game.engine.ecs.Component;
import scc210game.game.spawners.CombatWeapon;

public class CombatPlayerWeapon extends Component {

    public int damage = 0;
    public String imageLocation;

    public CombatPlayerWeapon(int damage, String imageLocation){
        this.damage = damage;
        this.imageLocation = imageLocation;
    }


    @Override
    public String serialize() {
        return null;
    }
}
