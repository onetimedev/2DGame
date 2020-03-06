package scc210game.game.components;

import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

public class CombatPlayerWeapon extends Component {

    public int damage = 0;
    public String imageLocation;

    public CombatPlayerWeapon(int damage, String imageLocation){
        this.damage = damage;
        this.imageLocation = imageLocation;
    }


    @Override
    public Jsonable serialize() {
        return null;
    }
}
