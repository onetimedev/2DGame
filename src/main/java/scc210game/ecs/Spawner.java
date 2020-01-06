package scc210game.ecs;

/**
 * Classes that insert components onto entities
 */
public interface Spawner {

    /**
     * Inject this spawner into the entity builder
     *
     * @param builder the {@link scc210game.ecs.World.EntityBuilder} to inject into
     * @return the entity builder passed in
     */
    World.EntityBuilder inject(World.EntityBuilder builder);
}
