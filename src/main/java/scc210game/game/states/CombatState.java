package scc210game.game.states;

import scc210game.engine.ecs.World;
import scc210game.engine.state.InputHandlingState;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.Transition;
import scc210game.game.spawners.CombatSpawner;

public class CombatState extends InputHandlingState {

    @Override
    public void onStart(World world) {
        world.activateCombat();
        world.entityBuilder().with(new CombatSpawner(true)).build();
        world.entityBuilder().with(new CombatSpawner(false)).build();

    }


    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        return super.handleEvent(evt, world);
    }
}
