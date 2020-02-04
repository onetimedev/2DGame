package scc210game.game.states.events;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.state.InputHandlingState;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.Transition;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.ui.spawners.ClickableTextBoxSpawner;
import scc210game.game.combat.*;

import java.time.Duration;

public class CombatState extends InputHandlingState {


    @Override
    public void onStart(World world) {
        world.entityBuilder().with(new CombatPlayer(true)).build();
        world.entityBuilder().with(new CombatPlayer(false)).build();

        world.entityBuilder().with(new ClickableTextBoxSpawner(0.5f, 0.0f, 0.1f, 0.1f, "fight", (Entity e, World w) -> {

            var player = w.applyQuery(Query.builder().require(Player.class).build()).findFirst().get();
            var pAttributes = w.fetchComponent(player, UITransform.class);

            var enemy = w.applyQuery(Query.builder().require(Enemy.class).build()).findFirst().get();
            var eAttributes = w.fetchComponent(enemy, UITransform.class);

            pAttributes.xPos += 0.03f;

            if(hasCollided(pAttributes, eAttributes))
            {
                System.out.println("collided");
            }




        })).build();

    }


    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        return super.handleEvent(evt, world);
    }



    private boolean hasCollided(UITransform rect1, UITransform rect2)
    {

        if (rect1.xPos < rect2.xPos + rect2.width &&
                rect1.xPos + rect1.width > rect2.xPos &&
                rect1.yPos < rect2.yPos + rect2.height &&
                rect1.yPos + rect1.height > rect2.yPos) {
            return true;
        }

        return false;

    }
}
