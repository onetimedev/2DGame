package scc210game.engine.ecs;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.events.Event;
import scc210game.engine.events.EventQueue;
import scc210game.engine.utils.Tuple2;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
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
    private final Map<Entity, Map<Class<? extends Component>, Component>> componentMaps;
    @Nonnull
    private final Map<Class<? extends Resource>, Resource> resourceMap;
    @Nonnull
    private final EntityAllocator entityAllocator;

    @Nonnull
    public final ECS ecs;
    @Nonnull
    public final EventQueue eventQueue;

    public boolean isCombatActive = false;
    public boolean activeAnimation = true;

    public World(@Nonnull ECS ecs) {
        this.ecs = ecs;
        this.entities = new ArrayList<>();
        this.entityComponents = new WeakHashMap<>();
        this.componentMaps = new WeakHashMap<>();
        this.resourceMap = new WeakHashMap<>();
        this.eventQueue = new EventQueue();
        this.entityAllocator = new EntityAllocator();
    }

    private World(@Nonnull ECS ecs, @Nonnull EntityAllocator entityAllocator) {
        this.ecs = ecs;
        this.entities = new ArrayList<>();
        this.entityComponents = new WeakHashMap<>();
        this.componentMaps = new WeakHashMap<>();
        this.resourceMap = new WeakHashMap<>();
        this.eventQueue = new EventQueue();
        this.entityAllocator = entityAllocator;
    }

    public Stream<Component> componentsOfEntity(Entity e) {
        return componentMaps.get(e).values().stream();
    }


    /**
     * Test if the given entity is in this world
     * @param e the entity to test
     * @return if the entity 'e' is in this world
     */
    public boolean hasEntity(Entity e) {
        return this.entities.contains(e);
    }

    void addEntity(Entity e, @Nonnull Collection<? extends Component> components) {
        this.entities.add(e);

        Set<Class<? extends Component>> componentTypes = components.stream()
                .map(Component::getClass)
                .collect(Collectors.toCollection(() -> Collections.newSetFromMap(new WeakHashMap<>())));

        this.entityComponents.put(e, componentTypes);

        for (final Component component : components) {
            this.addComponentToEntity(e, component);
        }
    }

    /**
     * Remove an entity from the World
     * @param e the Entity to remove
     */
    public void removeEntity(Entity e) {
        this.entities.remove(e);
        // NOTE(ben): we don't need to remove anything from entityComponents, etc
        // because they are weak key maps and so the entry is removed as soon as
        // the Entity to be removed is deallocated
    }

    /**
     * Add a Component to an Entity
     *
     * @param e         the Entity to add the Component to
     * @param component the Component to add
     */
    public void addComponentToEntity(Entity e, @Nonnull Component component) {
        assert this.entities.contains(e) : "Entity not added to world";

        Set<Class<? extends Component>> componentSet = this.entityComponents.computeIfAbsent(e, k -> new HashSet<>());
        componentSet.add(component.getClass());

        Map<Class<? extends Component>, Component> componentStorage =
                this.componentMaps.computeIfAbsent(e, k -> new HashMap<>());
        componentStorage.put(component.getClass(), component);
    }

    /**
     * Remove a component from an entity
     *
     * @param e             the Entity to remove the component from
     * @param componentType the type of component to remove
     */
    public void removeComponentFromEntity(Entity e, @Nonnull Class<? extends Component> componentType) {
        assert this.entities.contains(e) : "Entity not added to world";

        Set<Class<? extends Component>> componentSet = this.entityComponents.get(e);
        assert componentSet != null;
        componentSet.remove(componentType);

        Map<Class<? extends Component>, Component> componentStorage =
                this.componentMaps.get(e);
        assert componentStorage != null;
        componentStorage.remove(componentType);
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
        Resource resource = this.resourceMap.get(resourceType);
        assert resource != null : String.format("No resource for: %s", resourceType);
        return (T) resource;
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
     * Add a global resource
     *
     * @param r the {@link Resource} to add
     */
    public void addGlobalResource(@Nonnull Resource r) {
        this.ecs.addGlobalResource(r);
    }

    /**
     * Fetch a resource
     *
     * @param resourceType the type of {@link Resource} to fetch
     * @return the requested {@link Resource}
     */
    @Nonnull
    public <T extends Resource> T fetchGlobalResource(Class<T> resourceType) {
        return this.ecs.fetchGlobalResource(resourceType);
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
        var classComponentMap = this.componentMaps.get(e);
        assert classComponentMap != null : String.format("Entity: %s does not have component: %s", e, componentType.getName());
        return (T) classComponentMap.get(componentType);
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
            return q.testEntity(componentSet);
        }).sequential();
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

    public Jsonable serialize() {
        var entitesS = new JsonObject() {{
            World.this.entities.forEach(e -> {
                var json = new JsonObject() {{
                    World.this.componentMaps.get(e).forEach((klass, component) -> {
                        if (!component.shouldKeep())
                            return;
                        this.put(klass.getName(), component.serialize());
                    });
                }};
                this.put(String.valueOf(e.unsafeGetID()), json);
            });
        }};

        var resourcesS = new JsonObject() {{
            World.this.resourceMap.forEach((klass, resource) -> {
                if (!resource.shouldKeep())
                    return;
                this.put(klass.getName(), resource.serialize());
            });
        }};

        var now = Instant.now();

        var futureEvents = new JsonArray() {{
            World.this.eventQueue.fetchDelayedEvents().forEachRemaining((devt) -> {
                final Jsonable evt = new JsonObject() {{
                    this.put("deltaAtSave", Duration.between(now, devt.end).toString());
                    this.put("eClass", devt.e.getClass().getName());
                    this.put("e", devt.e.serialize());
                }};
                this.add(evt);
            });
        }};

        return new JsonObject(Map.of(
                "entities", entitesS,
                "resources", resourcesS,
                "futureEvents", futureEvents,
                "entityAllocator", this.entityAllocator.serialize()));
    }

    public static World deserialize(Jsonable j, ECS ecs) {
        var json = (JsonObject) j;

        var entities = new HashMap<Entity, List<Component>>();
        var resources = new ArrayList<Resource>();
        var futureEvents = new ArrayList<Tuple2<Instant, Event>>();

        var entitiesS = (JsonObject) json.get("entities");
        entitiesS.forEach((id, componentsS) -> {
            var entity = Entity.unsafeMakeEntity(Long.parseLong(id));

            var components = new ArrayList<Component>();
            //noinspection RedundantCast
            ((JsonObject) componentsS).forEach((compType, componentS) ->
                    components.add(SerDe.deserialize((Jsonable) componentS, compType, Component.class)));

            entities.put(entity, components);
        });

        var resourcesS = (JsonObject) json.get("resources");
        //noinspection RedundantCast
        resourcesS.forEach((resourceType, resourceS) ->
                resources.add(SerDe.deserialize((Jsonable) resourceS, resourceType, Resource.class)));

        var now = Instant.now();
        var futureEventsS = (JsonArray) json.get("futureEvents");
        futureEventsS.forEach(ej -> {
            var ejson = (JsonObject) ej;

            var eClass = (String) ejson.get("eClass");
            var e = SerDe.deserialize((Jsonable) ejson.get("e"), eClass, Event.class);

            var delta = Duration.parse((String) ejson.get("deltaAtSave"));
            var end = now.plus(delta);

            futureEvents.add(new Tuple2<>(end, e));
        });

        var entityAllocator = EntityAllocator.deserialize((Jsonable) json.get("entityAllocator"));

        var world = new World(ecs, entityAllocator);

        entities.forEach(world::addEntity);
        resources.forEach(world::addResource);

        futureEvents.forEach((t) -> world.eventQueue.broadcastAt(t.r, t.l));

        return world;
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
            this.entity = World.this.entityAllocator.allocate();
            this.components = new ArrayList<>();
            this.built = false;
        }

        /**
         * Add a {@link Component} to the initial components of the entity
         *
         * @param components the {@link Component}s to add
         * @return the current {@link EntityBuilder} instance (to allow chaining)
         */
        @Nonnull
        public EntityBuilder with(Component... components) {
            assert !this.built : "EntityBuilder already built";

            this.components.addAll(Arrays.asList(components));

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

            return spawner.inject(this, World.this);
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

    public void activateCombat()
    {
        this.isCombatActive = true;
    }

    public void deactivateCombat()
    {
        this.isCombatActive = false;
    }

    public boolean getCombatStatus()
    {
        return isCombatActive;
    }

    public void killAnimation()
    {
        this.activeAnimation = false;
    }

    public boolean getActiveAnimation()
    {
        return this.activeAnimation;
    }
}
