package scc210game.ecs;

/**
 * Stores metadata about components
 * <p>
 * So far this is just whether the component was modified by the last operation
 *
 * @param <T> The component this metadata is wrapping
 */
class ComponentMeta<T extends Component> {
    public final T component;
    public boolean isModified = false;

    public ComponentMeta(T component) {
        this.component = component;
    }
}
