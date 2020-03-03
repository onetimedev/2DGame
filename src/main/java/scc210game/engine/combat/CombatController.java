package scc210game.engine.combat;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;

public class CombatController extends Component {

    public CombatController(World world)
    {
        var scoring = world.applyQuery(Query.builder().require(Scoring.class).build()).findFirst().orElseThrow();
        var scores = world.fetchComponent(scoring, Scoring.class);

        while(true)
        {
            if(scores.getPlayerAbsHealth() <= 0)
            {
                //enemy won
                System.out.println("enemy won");
                break;
            }
        }
    }


}
