package scc210game.engine.state.trans;

import scc210game.engine.state.State;

/**
 * Swap the current state with another
 */
public class TransReplace implements Transition {
    public final State newState;

    public TransReplace(State newState) {
        this.newState = newState;
    }
}
