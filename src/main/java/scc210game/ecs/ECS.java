package scc210game.ecs;

import java.util.Collection;
import java.util.List;

/**
 * Wraps all the ECS parts
 */
public class ECS {
    private final World world;
    private final SystemExecutor executor;

    public ECS(List<? extends System> systems) {
        this.world = new World();
        this.executor = new SystemExecutor(systems);
    }

    public void addEntity(Entity e, Collection<? extends Component> components) {
        this.world.addEntity(e, components);
    }

    public void addComponentToEntity(Entity e, Component component) {
        this.world.addComponentToEntity(e, component);
    }

    public void runOnce() {
        this.executor.runOnce(this.world);
    }

    public <T extends Component> T fetchComponent(Entity e, Class<T> componentType) {
        return this.world.fetchComponent(e, componentType);
    }

    public World.EntityBuilder entityBuilder() {
        return this.world.entityBuilder();
    }
}
