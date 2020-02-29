package scc210game.engine.state;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.World;
import scc210game.engine.state.event.InputEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransNop;
import scc210game.engine.state.trans.Transition;

/**
 * A state where input handling is defaulted
 */
public class InputHandlingState extends State {
    static {
        register(InputHandlingState.class, (j) -> new InputHandlingState());
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof InputEvent) {
            world.ecs.eventQueue.broadcast(evt);
        }

        return TransNop.getInstance();
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject();
    }
}
