package scc210game.ecs;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A world tracks which components each entity has and stores those components.
 */
public class World {
    private final List<Entity> entities;
    private final Map<Entity, Set<Class<? extends Component>>> entityComponents;
    private final Map<Entity, Map<Class<? extends Component>, ComponentMeta<Component>>> componentMaps;

    World() {
        this.entities = new ArrayList<>();
        this.entityComponents = new HashMap<>();
        this.componentMaps = new HashMap<>();
    }

    void addEntity(Entity e, Collection<? extends Component> components) {
        this.entities.add(e);

        HashSet<Class<? extends Component>> componentTypes = components.stream()
                .map(Component::getClass)
                .collect(Collectors.toCollection(HashSet::new));

        this.entityComponents.put(e, componentTypes);

        for (final Component component : components) {
            this.addComponentToEntity(e, component);
        }
    }

    void addComponentToEntity(Entity e, Component component) {
        assert this.entities.contains(e) : "Entity not added to world";

        Set<Class<? extends Component>> componentSet = this.entityComponents.computeIfAbsent(e, k -> new HashSet<>());
        componentSet.add(component.getClass());

        Map<Class<? extends Component>, ComponentMeta<Component>> componentStorage =
                this.componentMaps.computeIfAbsent(e, k -> new HashMap<>());
        componentStorage.put(component.getClass(), new ComponentMeta<>(component));
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T fetchComponent(Entity e, Class<T> componentType) {
        return (T) this.componentMaps.get(e).get(componentType).component;
    }

    public void resetModifiedState(Entity e) {
        this.componentMaps.get(e).values().forEach(m -> m.isModified = false);
    }

    public void setModified(Entity e, Class<? extends Component> componentType) {
        this.setModifiedState(e, componentType, true);
    }

    @SuppressWarnings("BooleanParameter")
    public void setModifiedState(Entity e, Class<? extends Component> componentType, boolean state) {
        this.componentMaps.get(e).get(componentType).isModified = state;
    }

    Stream<Entity> applyQuery(Query q) {
        // I hope this is performant

        return this.entities.parallelStream().filter(e -> {
            Set<Class<? extends Component>> componentSet = this.entityComponents.get(e);
            Map<Class<? extends Component>, ComponentMeta<Component>> componentMap = this.componentMaps.get(e);

            return q.testEntity(componentSet, componentMap);
        });
    }

    public EntityBuilder entityBuilder() {
        return new EntityBuilder();
    }

    public class EntityBuilder {
        private final Entity entity;
        private final ArrayList<Component> components;
        private boolean built;

        public EntityBuilder() {
            this.entity = Entity.make();
            this.components = new ArrayList<>();
            this.built = false;
        }

        public EntityBuilder with(Component component) {
            assert !this.built : "EntityBuilder already built";

            this.components.add(component);

            return this;
        }

        public Entity build() {
            assert !this.built : "EntityBuilder already built";
            this.built = true;

            World.this.addEntity(this.entity, this.components);

            return this.entity;
        }
    }
}
