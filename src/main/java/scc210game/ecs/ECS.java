package scc210game.ecs;


import scc210game.state.State;
import scc210game.state.StateMachine;
import scc210game.state.event.StateEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wraps all the ECS parts
 */
public class ECS {
    @Nonnull
    private final SystemExecutor executor;
    @Nonnull
    private final StateMachine stateMachine;
    @Nonnull
    private final Map<Class<? extends Resource>, Resource> globalResources;

    /**
     * Construct the ECS wrapper from a list of systems to run
     *
     * @param systems      The systems that will be used
     * @param initialState The initial state the game will start with
     */
    public ECS(List<? extends System> systems, State initialState) {
        this.executor = new SystemExecutor(systems);
        this.stateMachine = new StateMachine(initialState);
        this.globalResources = new HashMap<>();
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
        this.executor.runOnce(this.stateMachine.currentWorld(), this.stateMachine.currentState().getClass());
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
     * Update the state of the game according to the passed event,
     * Then run one iteration of every system
     *
     * @param event the {@link StateEvent} to handle
     */
    public void runWithUpdateOnce(StateEvent event) {
        assert this.stateMachine.isRunning() : "State machine is not running";

        this.stateMachine.handle(event);
        this.executor.runOnce(this.stateMachine.currentWorld(), this.stateMachine.currentState().getClass());
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
}
