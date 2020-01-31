package scc210game.engine.state.trans;

import scc210game.engine.state.State;

/**
 * Clear the state stack and replace with the new state
 */
public class TransReplaceAll implements Transition {
    public final State newState;

    public TransReplaceAll(State newState) {
        this.newState = newState;
    }
}
