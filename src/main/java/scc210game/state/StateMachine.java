package scc210game.state;

import scc210game.ecs.World;
import scc210game.state.event.StateEvent;
import scc210game.state.trans.*;

import java.util.ArrayDeque;
import java.util.Deque;

public class StateMachine {
    private boolean running;
    private final Deque<StateData> states;

    public StateMachine(State currentState) {
        this.running = false;
        this.states = new ArrayDeque<>();
        this.states.addLast(new StateData(currentState));
    }

    public State currentState() {
        return this.states.getLast().state;
    }

    public World currentWorld() {
        return this.states.getLast().world;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void start() {
        if (this.running)
            return;

        this.running = true;

        this.currentState().onStart(this.currentWorld());
    }

    public void handle(StateEvent evt) {
        var t = this.currentState().handleEvent(evt, this.currentWorld());
        this.transition(t);
    }

    public void update() {
        var t = this.currentState().update(this.currentWorld());
        this.transition(t);
    }

    public void transition(Transition t) {
        if (t instanceof TransNop)
            return;

        if (t instanceof TransPop) {
            var s = this.states.removeLast();
            s.state.onStop(s.world);
            this.currentState().onResume();
        } else if (t instanceof TransPush) {
            var p = (TransPush) t;
            this.currentState().onPause();
            var s = new StateData(p.newState);
            this.states.addLast(s);
            s.state.onStart(s.world);
        } else if (t instanceof TransReplace) {
            var p = (TransReplace) t;
            var newS = new StateData(p.newState);
            var oldS = this.states.removeLast();
            oldS.state.onStop(oldS.world);
            this.states.addLast(newS);
            newS.state.onStart(newS.world);
        } else if (t instanceof TransQuit) {
            this.running = false;
        } else {
            System.err.println("Unknown transition type: " + t);
        }
    }

    static class StateData {
        public final State state;
        public final World world;

        public StateData(State state) {
            this.state = state;
            this.world = new World();
        }
    }
}
