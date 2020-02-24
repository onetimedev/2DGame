package scc210game.engine.render;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import org.jsfml.graphics.RenderWindow;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.utils.SerDeBase64;
import scc210game.engine.utils.SerializableTriConsumer;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Renderable extends Component {
	static {
		register(Renderable.class, j -> {
			var json = (JsonObject)	j;

			var includedViews = ((JsonArray) json.get("includedViews"))
					.stream()
					.map(f -> ViewType.valueOf((String) f))
					.collect(Collectors.toSet());

			@SuppressWarnings("unchecked")
			var renderFn = SerDeBase64.deserializeFromBase64((String) json.get("renderFn"),
					(Class<SerializableTriConsumer<Entity, RenderWindow, World>>)(Class<?>)SerializableTriConsumer.class);

			var height = (Integer) json.get("height");

			return new Renderable(includedViews, height, renderFn);
		});
	}

	/**
	 * Which views this renderable renders in
	 */
    public final Set<ViewType> includedViews;

	/**
	 * The function to call to render this entity
	 *
	 * Example:
	 * <pre>
	 *     {@code
	 *     (Entity e, RenderWindow rw, World w) -> {
	 *         rw.draw(...);
	 *     }
	 *     }
	 * </pre>
	 */
	public final SerializableTriConsumer<Entity, RenderWindow, World> renderFn;

	/**
	 * The height to render this at, renderables with higher height
	 * values render above those with lower.
	 */
	public final int height;

	/**
	 * @param includedViews Which views this renderable renders in
	 * @param height        The height to render this at, renderables with higher height
	 *                      values render above those with lower.
	 * @param renderFn      The function to call to render this entity
	 */
	public Renderable(Set<ViewType> includedViews, int height, SerializableTriConsumer<Entity, RenderWindow, World> renderFn) {
		this.includedViews = includedViews;
		this.renderFn = renderFn;
		this.height = height;
	}

	public Jsonable serialize() {
		var views = new JsonArray(this.includedViews
				.stream()
				.map(Enum::toString)
				.collect(Collectors.toCollection(ArrayList::new)));

		return new JsonObject(Map.of(
				"includedViews", views,
				"renderFn", SerDeBase64.serializeToBase64(this.renderFn),
				"height", this.height));
	}
}
