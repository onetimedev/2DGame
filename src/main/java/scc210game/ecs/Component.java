package scc210game.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The component interface
 */
public abstract class Component {
    private static final Map<Class<? extends Component>, Function<String, ? extends Component>> deserializers = new HashMap<>();
    private static final Map<String, Function<String, ? extends Component>> deserializersByName = new HashMap<>();

    /**
     * Register a component
     *
     * @param klass        the inheriting class
     * @param deserializer the method used to deserialize to this class
     */
    protected static void register(Class<? extends Component> klass, Function<String, ? extends Component> deserializer) {
        String name = klass.getName();
        Component.deserializers.put(klass, deserializer);
        Component.deserializersByName.put(name, deserializer);
    }

    /**
     * Serialize a component
     *
     * @return a string representing the deserialized component
     */
    public abstract String serialize();

    /**
     * @param s   The string to deserialize
     * @param <T> the type to deserialize to
     * @return The deserialized component
     */
    @SuppressWarnings("unchecked")
    public static <T extends Component> T deserialize(String s, Class<T> type) {
        return (T) Component.deserializers.get(type).apply(s);
    }

    /**
     * @param s   The string to deserialize
     * @param <T> the type to deserialize to
     * @return The deserialized component
     */
    @SuppressWarnings("unchecked")
    public static <T extends Component> T deserialize(String s, String type) {
        return (T) Component.deserializersByName.get(type).apply(s);
    }
}
