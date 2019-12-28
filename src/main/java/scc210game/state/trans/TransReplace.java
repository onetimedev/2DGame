package scc210game.state.trans;

import scc210game.state.State;

/**
 * Swap the current state with another
 */
public class TransReplace implements Transition {
    public final State newState;

    public TransReplace(State newState) {
        this.newState = newState;
    }
}
