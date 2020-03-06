package scc210game.engine.ecs;

/**
 * Classes that insert components onto entities
 */
public interface Spawner {

    /**
     * Inject this spawner into the entity builder
     *
     * @param builder the {@link World.EntityBuilder} to inject into
     * @param world the World the entity is being built in
     * @return the entity builder passed in
     */
    World.EntityBuilder inject(World.EntityBuilder builder, World world);
}
