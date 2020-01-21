package scc210game.state;

import scc210game.ecs.World;
import scc210game.events.EventQueue;
import scc210game.state.event.InputEvent;
import scc210game.state.event.StateEvent;
import scc210game.state.trans.TransNop;
import scc210game.state.trans.Transition;

/**
 * A state where input handling is defaulted
 */
public interface InputHandlingState extends State {
    @Override
    default Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof InputEvent) {
            EventQueue.broadcast(evt);
        }

        return TransNop.getInstance();
    }
}
