package scc210game.engine.state;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.ECS;
import scc210game.engine.ecs.World;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;

public class StateMachine {
    private final ECS ecs;
    private boolean running;
    private final Deque<StateData> states;

    public StateMachine(State currentState, ECS ecs) {
        this.ecs = ecs;
        this.running = false;
        this.states = new ArrayDeque<>();
        this.states.addLast(new StateData(currentState, ecs));
    }

    public State currentState() {
        return this.currentSD().state;
    }

    public Instant lastRunInstant() {
        return this.currentSD().tLastRun;
    }

    public Instant lastRunInstant(Instant now) {
        var c = this.currentSD();
        var lr = c.tLastRun;
        c.tLastRun = now;
        return lr;
    }

    public World currentWorld() {
        return this.currentSD().world;
    }

    private StateData currentSD() {
        return this.states.getLast();
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
            this.currentSD().resume();
        } else if (t instanceof TransPush) {
            var p = (TransPush) t;
            this.currentSD().pause();
            var s = new StateData(p.newState, this.ecs);
            this.states.addLast(s);
            s.state.onStart(s.world);
        } else if (t instanceof TransReplace) {
            var p = (TransReplace) t;
            var newS = new StateData(p.newState, this.ecs);
            var oldS = this.states.removeLast();
            oldS.state.onStop(oldS.world);
            this.states.addLast(newS);
            newS.state.onStart(newS.world);
        } else if (t instanceof TransReplaceAll) {
            var p = (TransReplaceAll) t;
            var newS = new StateData(p.newState, this.ecs);
            while (!this.states.isEmpty()) {
                var oldS = this.states.removeLast();
                oldS.state.onStop(oldS.world);
            }
            this.states.addLast(newS);
            newS.state.onStart(newS.world);
        } else if (t instanceof TransQuit) {
            this.running = false;
        } else {
            throw new RuntimeException("Unknown transition type: " + t);
        }
    }

    public Jsonable serialize() {
        return new JsonArray() {{
            StateMachine.this.states.forEach(sd -> {
                final Jsonable sdjson = new JsonObject() {{
                    this.put("state", sd.state.serialize());
                    this.put("world", sd.world.serialize());
                    this.put("tLastRun", sd.tLastRun.toString());
                    this.put("tOnPause", sd.tOnPause == null ? null : sd.tOnPause.toString());
                }};

                this.add(sdjson);
            });
        }};
    }

    static class StateData {
        public final State state;
        public final World world;
        @Nonnull
        public Instant tLastRun = Instant.now();
        @Nullable
        public Instant tOnPause;

        public StateData(State state, ECS ecs) {
            this.state = state;
            this.world = new World(ecs);
        }

        void pause() {
            this.tOnPause = Instant.now();
            this.state.onPause();
        }

        void resume() {
            var now = Instant.now();
            assert this.tOnPause != null;
            var td = Duration.between(this.tOnPause, now);
            this.tOnPause = null;
            this.world.eventQueue.patchDelayDelta(td);
            this.tLastRun = this.tLastRun.plus(td);
            this.state.onResume();
        }
    }
}
