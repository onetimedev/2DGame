package scc210game.ecs;

class ComponentMeta<T extends Component> {
    public final T component;
    public boolean isModified = false;

    public ComponentMeta(T component) {
        this.component = component;
    }
}
