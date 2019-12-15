package scc210game.ecs;

/**
 * Systems in the ECS model
 */
public abstract class System {
    private static long idCounter = 0;

    final long id;

    public System() {
        this.id = System.idCounter++;
    }

    public void run(World world) {
        // TODO: query entities from world, process them
    }

    /**
     * Process an entity in the world.
     * @param e The entity to process
     */
    public abstract void actOnEntity(Entity e);
}
