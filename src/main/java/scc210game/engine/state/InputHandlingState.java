package scc210game.engine.state;

import scc210game.engine.ecs.World;
import scc210game.engine.events.EventQueue;
import scc210game.engine.state.event.InputEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransNop;
import scc210game.engine.state.trans.Transition;

/**
 * A state where input handling is defaulted
 */
public class InputHandlingState extends State {
    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof InputEvent) {
            EventQueue.broadcast(evt);
        }

        return TransNop.getInstance();
    }
}
