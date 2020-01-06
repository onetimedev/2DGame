package scc210game.state.trans;

import scc210game.state.State;

/**
 * Push a new state onto the state stack
 */
public class TransPush implements Transition {
    public final State newState;

    public TransPush(State newState) {
        this.newState = newState;
    }
}
