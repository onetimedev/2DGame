package scc210game.engine.state;

import scc210game.engine.ecs.World;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransNop;
import scc210game.engine.state.trans.Transition;

/**
 * A state is well, a possible state the game is in.
 * <p>
 * Each state has an associated world, this means that every entity is local to the state they are in.
 * Thus when the game is in the 'paused' state, no entities in the 'running' state or 'menu' state
 * are accessible
 */
public abstract class State {
    /**
     * Called when this game state is entered
     *
     * @param world the world associated with this state
     */
    public void onStart(World world) {
    }

    /**
     * Called when this game state is stopped
     *
     * @param world the world associated with this state
     */
    public void onStop(World world) {
    }

    /**
     * Called when another game state is put on top of this state
     */
    public void onPause() {
    }

    /**
     * Called when this game state is unpaused
     */
    public void onResume() {
    }

    /**
     * Called when this state needs to handle an event
     *
     * @param evt   the Event to handle
     * @param world the world associated with this state
     * @return a Transition object describing what to do
     */
    public Transition handleEvent(StateEvent evt, World world) {
        return TransNop.getInstance();
    }

    /**
     * Called as often as possible
     *
     * @param world the world associated with this state
     * @return a Transition object describing what to do
     */
    public Transition update(World world) {
        return TransNop.getInstance();
    }

    /**
     * Called every frame
     *
     * @return a Transition object describing what to do
     */
    public Transition onFrame() {
        return TransNop.getInstance();
    }
}
