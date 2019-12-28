package scc210game.state;

import scc210game.state.event.StateEvent;
import scc210game.state.trans.*;

import java.util.ArrayDeque;
import java.util.Deque;

public class StateMachine {
    private boolean running;
    private final Deque<State> states;

    public StateMachine(State currentState) {
        this.running = false;
        this.states = new ArrayDeque<>();
        this.states.addLast(currentState);
    }

    public State currentState() {
        return this.states.getLast();
    }

    public void start() {
        if (this.running)
            return;

        this.currentState().onStart();
    }

    public void handle(StateEvent evt) {
        var t = this.currentState().handleEvent(evt);
        this.transition(t);
    }

    public void update() {
        var t = this.currentState().update();
        this.transition(t);
    }

    public void transition(Transition t) {
        if (t instanceof TransNop)
            return;

        if (t instanceof TransPop) {
            var s = this.states.removeLast();
            s.onStop();
            this.currentState().onResume();
        } else if (t instanceof TransPush) {
            var p = (TransPush) t;
            this.currentState().onPause();
            var s = p.newState;
            this.states.addLast(s);
            s.onStart();
        } else if (t instanceof TransReplace) {
            var p = (TransReplace) t;
            var newS = p.newState;
            var oldS = this.states.removeLast();
            oldS.onStop();
            this.states.addLast(newS);
            newS.onStart();
        } else if (t instanceof TransQuit) {
            this.running = false;
        } else {
            System.err.println("Unknown transition type: " + t);
        }
    }
}
