package scc210game.ecs;


import scc210game.state.State;
import scc210game.state.StateMachine;
import scc210game.state.event.StateEvent;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Wraps all the ECS parts
 */
public class ECS {
    @Nonnull
    private final World world;
    @Nonnull
    private final SystemExecutor executor;

    private final StateMachine stateMachine;

    /**
     * Construct the ECS wrapper from a list of systems to run
     *
     * @param systems      The systems that will be used
     * @param initialState The initial state the game will start with
     */
    public ECS(List<? extends System> systems, State initialState) {
        this.world = new World();
        this.executor = new SystemExecutor(systems);
        this.stateMachine = new StateMachine(initialState);
    }

    /**
     * Start the game
     */
    public void start() {
        this.stateMachine.start();
    }

    /**
     * Add an entity to the world with a set of components
     *
     * @param e          the {@link Entity} to add to the world
     * @param components the {@link Component}s the entity should start with
     */
    public void addEntity(Entity e, @Nonnull Collection<? extends Component> components) {
        this.world.addEntity(e, components);
    }

    /**
     * Add a component to an entity
     *
     * @param e         the {@link Entity} to add the component to
     * @param component the {@link Component} to add to the entity
     */
    public void addComponentToEntity(Entity e, @Nonnull Component component) {
        this.world.addComponentToEntity(e, component);
    }

    /**
     * Update the state of the game,
     * Then run one iteration of every system
     */
    public void runOnce() {
        this.stateMachine.update();
        this.executor.runOnce(this.world, this.stateMachine.currentState().getClass());
    }

    /**
     * Update the state of the game according to the passed event,
     * Then run one iteration of every system
     *
     * @param event the {@link StateEvent} to handle
     */
    public void runWithUpdateOnce(StateEvent event) {
        this.stateMachine.handle(event);
        this.executor.runOnce(this.world, this.stateMachine.currentState().getClass());
    }

    /**
     * Fetch a component for an entity
     *
     * @param e             the {@link Entity} to fetch the component of
     * @param componentType the type of {@link Component} to fetch
     * @param <T>           the class of {@link Component} to fetch
     * @return the requested {@link Component}
     */
    @Nonnull
    public <T extends Component> T fetchComponent(Entity e, Class<T> componentType) {
        return this.world.fetchComponent(e, componentType);
    }

    /**
     * Flag an entity's component as modified
     *
     * @param e             the {@link Entity} for which the component to be flagged as modified belongs to
     * @param componentType the type of {@link Component} to flag as modified
     */
    public void setModified(Entity e, Class<? extends Component> componentType) {
        this.world.setModified(e, componentType);
    }

    /**
     * Set the modified state of an entity's component
     *
     * @param e             the {@link Entity} for which the component to set the modified flag belongs to
     * @param componentType the type of {@link Component} to set the modified flag on
     * @param state         the state to set the modified flag to
     */
    @SuppressWarnings("BooleanParameter")
    public void setModifiedState(Entity e, Class<? extends Component> componentType, boolean state) {
        this.world.setModifiedState(e, componentType, state);
    }

    /**
     * Construct an {@link World.EntityBuilder} used to build an entity with a set of components
     *
     * @return the constructed {@link World.EntityBuilder}
     */
    @Nonnull
    public World.EntityBuilder entityBuilder() {
        return this.world.entityBuilder();
    }
}
