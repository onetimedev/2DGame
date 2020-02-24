package scc210game.engine.ecs;

import com.github.cliftonlabs.json_simple.Jsonable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Class representing types that can be serialized and deserialized
 */
public abstract class SerDe {
    private static final Map<Class<? extends SerDe>, Function<Jsonable, ? extends SerDe>> deserializers = new HashMap<>();
    private static final Map<String, Function<Jsonable, ? extends SerDe>> deserializersByName = new HashMap<>();

    /**
     * Register a component
     *
     * @param klass        the inheriting class
     * @param deserializer the method used to deserialize to this class
     */
    protected static void register(Class<? extends SerDe> klass, Function<Jsonable, ? extends SerDe> deserializer) {
        String name = klass.getName();
        SerDe.deserializers.put(klass, deserializer);
        SerDe.deserializersByName.put(name, deserializer);
    }

    /**
     * @param j   the json data to deserialize from
     * @param <T> the type to deserialize to
     * @return The deserialized component
     */
    public static <T extends SerDe> T deserialize(Jsonable j, Class<T> type) {
        // java.lang.System.err.println("type=" + type + ", result=" + SerDe.deserializers.get(type).apply(j));
        return type.cast(SerDe.deserializers.get(type).apply(j));
    }

    /**
     * @param j   the json data to deserialize from
     * @param <T> the type to deserialize to
     * @return The deserialized component
     */
    public static <T extends SerDe> T deserialize(Jsonable j, String type, Class<T> parentType) {
        return parentType.cast(SerDe.deserializersByName.get(type).apply(j));
    }

    /**
     * Serialize a component
     *
     * @return a json object representing the deserialized component
     */
    public Jsonable serialize() {
        throw new RuntimeException("You wanted to deserialize a type: " +
                this.getClass().getName() +
                ", but it doesn't have serialize implemented");
    }
}
