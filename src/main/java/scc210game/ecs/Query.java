package scc210game.ecs;

import java.util.*;

/**
 * Represents a query used to filter entities that have a set of components.
 */
public class Query {
    private final Set<Class<? extends Component>> mustHave;
    private final Set<Class<? extends Component>> mustBeModified;

    /**
     * Create a query
     *
     * @param mustHave       the set of components that entities must have to pass the filter
     * @param mustBeModified components that must have been modified to pass the filter
     */
    public Query(List<Class<? extends Component>> mustHave, List<Class<? extends Component>> mustBeModified) {
        this.mustHave = new HashSet<>(mustHave);
        this.mustBeModified = new HashSet<>(mustBeModified);
    }

    boolean testEntity(Collection<Class<? extends Component>> componentSet, Map<Class<? extends Component>, ? extends ComponentMeta<Component>> componentData) {
        if (!componentSet.containsAll(this.mustHave))
            return false;

        for (final Class<? extends Component> compType : this.mustHave) {
            var mustBeModified = this.mustBeModified.contains(compType);
            var meta = componentData.get(compType);

            if (!meta.isModified && mustBeModified)
                return false;
        }

        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ArrayList<Class<? extends Component>> mustHave;
        private final ArrayList<Class<? extends Component>> mustBeModified;

        public Builder() {
            this.mustHave = new ArrayList<>();
            this.mustBeModified = new ArrayList<>();
        }

        public Builder require(Class<? extends Component> compType) {
            this.mustHave.add(compType);

            return this;
        }

        public Builder requireModified(Class<? extends Component> compType) {
            this.mustHave.add(compType);
            this.mustBeModified.add(compType);

            return this;
        }

        public Query build() {
            return new Query(this.mustHave, this.mustBeModified);
        }
    }
}
