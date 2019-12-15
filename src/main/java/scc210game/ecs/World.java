package scc210game.ecs;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A world tracks which components each entity has and stores those components.
 */
public class World {
    private final Map<Entity, Set<Class<? extends Component>>> entityComponents;
    private final Map<Class<? extends Component>, Map<Entity, Component>> componentMaps;

    public World() {
        this.entityComponents = new HashMap<>();
        this.componentMaps = new HashMap<>();
    }

    public void addEntity(Entity e, Collection<? extends Component> components) {
        HashSet<Class<? extends Component>> componentTypes = components.stream()
                .map(Component::getClass)
                .collect(Collectors.toCollection(HashSet::new));

        this.entityComponents.put(e, componentTypes);

        for (final Component component : components) {
            this.addComponentToEntity(e, component);
        }
    }

    public void addComponentToEntity(Entity e, Component component) {
        Set<Class<? extends Component>> componentSet = this.entityComponents.computeIfAbsent(e, k -> new HashSet<>());
        componentSet.add(component.getClass());

        Map<Entity, Component> componentStorage =
                this.componentMaps.computeIfAbsent(component.getClass(), k -> new HashMap<>());
        componentStorage.put(e, component);
    }
}
