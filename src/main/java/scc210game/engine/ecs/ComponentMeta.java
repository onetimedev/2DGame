package scc210game.engine.ecs;

import javax.annotation.Nonnull;

/**
 * Stores metadata about components
 * <p>
 * So far this is just whether the component was modified by the last operation
 *
 * @param <T> The component this metadata is wrapping
 */
class ComponentMeta<T extends Component> {
    @Nonnull
    public final T component;
    public boolean isModified = false;

    public ComponentMeta(@Nonnull T component) {
        this.component = component;
    }
}
