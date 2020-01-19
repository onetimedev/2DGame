package scc210game.ecs;


import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A world tracks which components each entity has and stores those components.
 */
public class World {
    @Nonnull
    private final List<Entity> entities;
    @Nonnull
    private final Map<Entity, Set<Class<? extends Component>>> entityComponents;
    @Nonnull
    private final Map<Entity, Map<Class<? extends Component>, ComponentMeta<Component>>> componentMaps;
    @Nonnull
    private final Map<Class<? extends Resource>, Resource> resourceMap;

    public World() {
        this.entities = new ArrayList<>();
        this.entityComponents = new HashMap<>();
        this.componentMaps = new HashMap<>();
        this.resourceMap = new HashMap<>();
    }

    void addEntity(Entity e, @Nonnull Collection<? extends Component> components) {
        this.entities.add(e);

        HashSet<Class<? extends Component>> componentTypes = components.stream()
                .map(Component::getClass)
                .collect(Collectors.toCollection(HashSet::new));

        this.entityComponents.put(e, componentTypes);

        for (final Component component : components) {
            this.addComponentToEntity(e, component);
        }
    }

    void addComponentToEntity(Entity e, @Nonnull Component component) {
        assert this.entities.contains(e) : "Entity not added to world";

        Set<Class<? extends Component>> componentSet = this.entityComponents.computeIfAbsent(e, k -> new HashSet<>());
        componentSet.add(component.getClass());

        Map<Class<? extends Component>, ComponentMeta<Component>> componentStorage =
                this.componentMaps.computeIfAbsent(e, k -> new HashMap<>());
        componentStorage.put(component.getClass(), new ComponentMeta<>(component));
    }

    /**
     * Add a resource
     *
     * @param r the {@link Resource} to add
     */
    public void addResource(@Nonnull Resource r) {
        this.resourceMap.put(r.getClass(), r);
    }

    /**
     * Test if a resource exists
     *
     * @param resourceType the type of {@link Resource} to test for existence
     */
    public boolean hasResource(Class<? extends Resource> resourceType) {
        return this.resourceMap.containsKey(resourceType);
    }

    /**
     * Fetch a resource
     *
     * @param resourceType the type of {@link Resource} to fetch
     * @param <T>          the class of {@link Resource} to fetch
     * @return the requested {@link Resource}
     * @apiNote this explodes if the resource doesn't exist
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    public <T extends Resource> T fetchResource(Class<T> resourceType) {
        return (T) this.resourceMap.get(resourceType);
    }

    /**
     * Fetch a resource with a producer if the resource does not exist
     *
     * @param resourceType the type of {@link Resource} to fetch
     * @param <T>          the class of {@link Resource} to fetch
     * @return the requested {@link Resource}
     * @apiNote this explodes if the resource doesn't exist
     */
    @SuppressWarnings({"unchecked", "BoundedWildcard"})
    @Nonnull
    public <T extends Resource> T fetchResource(Class<T> resourceType, Supplier<T> supplier) {
        return (T) this.resourceMap.computeIfAbsent(resourceType, k -> supplier.get());
    }

    /**
     * Test if a component exists for a given entity
     *
     * @param e             the {@link Entity} to test the component of
     * @param componentType the type of {@link Component} to test for existence
     */
    public boolean hasComponent(Entity e, Class<? extends Component> componentType) {
        var components = this.componentMaps.get(e);

        if (components == null) {
            return false;
        }

        return components.containsKey(componentType);
    }

    /**
     * Fetch a component for an entity
     *
     * @param e             the {@link Entity} to fetch the component of
     * @param componentType the type of {@link Component} to fetch
     * @param <T>           the class of {@link Component} to fetch
     * @return the requested {@link Component}
     * @apiNote this explodes if the component doesn't exist
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public <T extends Component> T fetchComponent(Entity e, Class<T> componentType) {
        return (T) this.componentMaps.get(e).get(componentType).component;
    }

    /**
     * Reset the modification state of all components for the given {@link Entity} to unmodified
     *
     * @param e the {@link Entity} to modify
     */
    public void resetModifiedState(Entity e) {
        this.componentMaps.get(e).values().forEach(m -> m.isModified = false);
    }

    /**
     * Flag an entity's component as modified
     *
     * @param e             the {@link Entity} for which the component to be flagged as modified belongs to
     * @param componentType the type of {@link Component} to flag as modified
     */
    public void setModified(Entity e, Class<? extends Component> componentType) {
        this.setModifiedState(e, componentType, true);
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
        this.componentMaps.get(e).get(componentType).isModified = state;
    }

    /**
     * Apply a query to the world, fetching all the entities matching the conditions of the query
     *
     * @param q the {@link Query} to use
     * @return a {@link Stream<Entity>} of entities that match the query
     */
    public Stream<Entity> applyQuery(@Nonnull Query q) {
        // I hope this is performant

        return this.entities.parallelStream().filter(e -> {
            Set<Class<? extends Component>> componentSet = this.entityComponents.get(e);
            Map<Class<? extends Component>, ComponentMeta<Component>> componentMap = this.componentMaps.get(e);

            return q.testEntity(componentSet, componentMap);
        });
    }

    /**
     * Construct an {@link EntityBuilder} used to build an entity with a set of components
     *
     * @return the constructed {@link EntityBuilder}
     */
    @Nonnull
    public EntityBuilder entityBuilder() {
        return new EntityBuilder();
    }

    /**
     * A helper class for constructing entities
     */
    public class EntityBuilder {
        @Nonnull
        private final Entity entity;
        @Nonnull
        private final ArrayList<Component> components;
        private boolean built;

        public EntityBuilder() {
            this.entity = Entity.make();
            this.components = new ArrayList<>();
            this.built = false;
        }

        /**
         * Add a {@link Component} to the initial components of the entity
         *
         * @param component the {@link Component} to add
         * @return the current {@link EntityBuilder} instance (to allow chaining)
         */
        @Nonnull
        public EntityBuilder with(Component component) {
            assert !this.built : "EntityBuilder already built";

            this.components.add(component);

            return this;
        }

        /**
         * Invoke a {@link Spawner} with this entity
         *
         * @param spawner the {@link Spawner} to use
         * @return the current {@link EntityBuilder} instance (to allow chaining)
         */
        @Nonnull
        public EntityBuilder with(Spawner spawner) {
            assert !this.built : "EntityBuilder already built";

            return spawner.inject(this);
        }

        /**
         * Construct the entity
         *
         * @return the constructed {@link Entity}
         */
        @Nonnull
        public Entity build() {
            assert !this.built : "EntityBuilder already built";
            this.built = true;

            World.this.addEntity(this.entity, this.components);

            return this.entity;
        }
    }
}
