package scc210game.engine.ecs;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.events.EventQueue;
import scc210game.engine.state.State;
import scc210game.engine.state.StateMachine;
import scc210game.engine.state.event.StateEvent;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Wraps all the ECS parts
 */
public class ECS {
    @Nonnull
    private final List<? extends System> systems;
    @Nonnull
    private final StateMachine stateMachine;
    @Nonnull
    private final Map<Class<? extends Resource>, Resource> globalResources;

    @Nonnull
    public final EventQueue eventQueue;

    public Jsonable toReload = null;

    /**
     * Construct the ECS wrapper from a list of systems to run
     *
     * @param systems      The systems that will be used, as functions that consume ECS and produce a system object
     * @param initialState The initial state the game will start with
     */
    public ECS(@Nonnull List<Function<ECS, ? extends System>> systems, @Nonnull State initialState) {
        this.stateMachine = new StateMachine(initialState, this);
        this.globalResources = new HashMap<>();
        this.eventQueue = new EventQueue();
        this.systems = systems.stream().map((f) -> f.apply(this)).collect(Collectors.toList());
    }

    /**
     * Test if the ecs is running
     * @return True if the engine is running, False if it is not
     */
    public boolean isRunning() {
        return this.stateMachine.isRunning();
    }

    /**
     * Start the game
     */
    public void start() {
        this.stateMachine.start();
    }

    /**
     * Update the state of the game,
     * Then run one iteration of every system
     */
    public void runOnce() {
        assert this.stateMachine.isRunning() : "State machine is not running";

        this.stateMachine.update();
        Instant now = Instant.now();
        Duration delta = Duration.between(this.stateMachine.lastRunInstant(now), now);

        for (final System s : this.systems) {
            s.run(this.stateMachine.currentWorld(), delta);
        }

        if (this.toReload != null) {
            this.deserializeAndReplaceInner(this.toReload);
            this.toReload = null;
        }
    }

    /**
     * Fetch the current world
     *
     * @return the current world
     */
    public World getCurrentWorld() {
        return this.stateMachine.currentWorld();
    }

    /**
     * Update the state of the game according to the passed event
     *
     * @param event the {@link StateEvent} to handle
     */
    public void acceptEvent(StateEvent event) {
        assert this.stateMachine.isRunning() : "State machine is not running";

        this.stateMachine.handle(event);
    }

    /**
     * Add a global resource
     *
     * @param r the {@link Resource} to add
     */
    public void addGlobalResource(@Nonnull Resource r) {
        this.globalResources.put(r.getClass(), r);
    }

    /**
     * Fetch a resource
     *
     * @param resourceType the type of {@link Resource} to fetch
     * @param <T>          the class of {@link Resource} to fetch
     * @return the requested {@link Resource}
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    public <T extends Resource> T fetchGlobalResource(Class<T> resourceType) {
        return (T) this.globalResources.get(resourceType);
    }

    /**
     * Serialize the ecs to a string
     *
     * @return a string representing the serialized ECS
     */
    public Jsonable serialize() {
        var resourcesS = new JsonObject() {{
            globalResources.forEach((klass, resource) -> {
                if (!resource.shouldKeep())
                    return;
                this.put(klass.getName(), resource.serialize());
            });
        }};

        return new JsonObject() {{
            this.put("globalResources", resourcesS);
            this.put("states", ECS.this.stateMachine.serialize());
        }};
    }

    private void deserializeAndReplaceInner(Jsonable j) {
        var json = (JsonObject) j;

        var resourcesS = (JsonObject) json.get("resources");
        //noinspection RedundantCast
        resourcesS.forEach((resourceType, resourceS) -> {
            var resource = SerDe.deserialize((Jsonable) resourceS, resourceType, Resource.class);
            globalResources.put(resource.getClass(), resource);
        });

        this.stateMachine.deserializeAndReplace((JsonArray) json.get("states"));
    }

    public void deserializeAndReplace(Jsonable json) {
        this.toReload = json;
    }
}
