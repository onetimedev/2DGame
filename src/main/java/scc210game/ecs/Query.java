package scc210game.ecs;

import scc210game.state.State;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Represents a query used to filter entities that have a set of components.
 */
public class Query {
    @Nonnull
    private final Set<Class<? extends Component>> mustHave;
    @Nonnull
    private final Set<Class<? extends Component>> mustBeModified;

    /**
     * Create a query
     *
     * @param mustHave       the set of components that entities must have to pass the filter
     * @param mustBeModified components that must have been modified to pass the filter
     */
    public Query(@Nonnull List<Class<? extends Component>> mustHave,
                 @Nonnull List<Class<? extends Component>> mustBeModified) {
        this.mustHave = new HashSet<>(mustHave);
        this.mustBeModified = new HashSet<>(mustBeModified);
    }

    boolean testEntity(@Nonnull Collection<Class<? extends Component>> componentSet, @Nonnull Map<Class<? extends Component>, ? extends ComponentMeta<Component>> componentData) {
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
    @Nonnull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A helper class for constructing queries
     */
    public static class Builder {
        @Nonnull
        private final ArrayList<Class<? extends Component>> mustHave;
        @Nonnull
        private final ArrayList<Class<? extends Component>> mustBeModified;
        @Nonnull
        private final ArrayList<Class<? extends State>> stateBlackList;
        private boolean built;

        public Builder() {
            this.mustHave = new ArrayList<>();
            this.mustBeModified = new ArrayList<>();
            this.stateBlackList = new ArrayList<>();
            this.built = false;
        }

        /**
         * Add to the set of components this query needs
         *
         * @param compType the type of {@link Component} to add to the set of required components
         * @return the current {@link Builder} instance (to allow chaining)
         */
        @Nonnull
        public Builder require(Class<? extends Component> compType) {
            assert !this.built : "builder already build";

            this.mustHave.add(compType);

            return this;
        }

        /**
         * Add to the set of states this query cannot match while in.
         *
         * @param state the type of {@link State} that is disallowed
         * @return the current {@link Builder} instance (to allow chaining)
         **/
        @Nonnull
        public Builder notInState(Class<? extends State> state) {
            assert !this.built : "builder already build";

            this.stateBlackList.add(state);

            return this;
        }

        /**
         * Add to the set of components that must have been modified that this query needs
         *
         * @param compType the type of {@link Component} to add to the set of modified required components
         * @return the current {@link Builder} instance (to allow chaining)
         */
        @Nonnull
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
        @Nonnull
        public Query build() {
            assert !this.built : "Builder already build";
            this.built = true;


            return new Query(this.mustHave, this.mustBeModified);
        }
    }
}
