package scc210game.engine.ecs;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Represents a query used to filter entities that have a set of components.
 */
public class Query {
    @Nonnull
    private final Set<Class<? extends Component>> mustHave;

    /**
     * Create a query
     *
     * @param mustHave the set of components that entities must have to pass the filter
     */
    public Query(@Nonnull List<Class<? extends Component>> mustHave) {
        this.mustHave = new HashSet<>(mustHave);
    }

    boolean testEntity(@Nonnull Collection<Class<? extends Component>> componentSet) {
        return componentSet.containsAll(this.mustHave);
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
        private boolean built;

        public Builder() {
            this.mustHave = new ArrayList<>();
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
         * Construct the query
         *
         * @return the constructed {@link Query}
         */
        @Nonnull
        public Query build() {
            assert !this.built : "Builder already build";
            this.built = true;


            return new Query(this.mustHave);
        }
    }
}
