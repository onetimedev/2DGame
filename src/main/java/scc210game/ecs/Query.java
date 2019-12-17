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

    /**
     * Get a {@link Builder} instance
     *
     * @return the new {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A helper class for constructing queries
     */
    public static class Builder {
        private final ArrayList<Class<? extends Component>> mustHave;
        private final ArrayList<Class<? extends Component>> mustBeModified;
        private boolean built;

        public Builder() {
            this.mustHave = new ArrayList<>();
            this.mustBeModified = new ArrayList<>();
            this.built = false;
        }

        /**
         * Add to the set of components this query needs
         *
         * @param compType the type of {@link Component} to add to the set of required components
         * @return the current {@link Builder} instance (to allow chaining)
         */
        public Builder require(Class<? extends Component> compType) {
            assert !this.built : "Builder already build";

            this.mustHave.add(compType);

            return this;
        }

        /**
         * Add to the set of components that must have been modified that this query needs
         *
         * @param compType the type of {@link Component} to add to the set of modified required components
         * @return the current {@link Builder} instance (to allow chaining)
         */
        public Builder requireModified(Class<? extends Component> compType) {
            assert !this.built : "Builder already build";

            this.mustHave.add(compType);
            this.mustBeModified.add(compType);

            return this;
        }

        /**
         * Construct the query
         *
         * @return the constructed {@link Query}
         */
        public Query build() {
            assert !this.built : "Builder already build";
            this.built = true;


            return new Query(this.mustHave, this.mustBeModified);
        }
    }
}
