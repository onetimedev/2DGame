package scc210game.engine.combat;

import scc210game.engine.audio.Audio;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.utils.ResourceLoader;

public class CombatController extends Component {

    public CombatController(World world)
    {
        var scoring = world.applyQuery(Query.builder().require(Scoring.class).build()).findFirst().orElseThrow();
        var scores = world.fetchComponent(scoring, Scoring.class);
        Audio au = new Audio();

        while(true)
        {
            if(scores.getPlayerAbsHealth() <= 0)
            {
                au.playSound(ResourceLoader.resolve("sounds/hero_death.wav"), false);
                //enemy won
                System.out.println("enemy won");
                break;
            }
        }
    }


}
