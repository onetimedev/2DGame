package scc210game.render;


import org.jsfml.graphics.RenderWindow;
import scc210game.ecs.Component;
import scc210game.ecs.Entity;
import scc210game.ecs.World;
import scc210game.utils.TriConsumer;

import java.util.Set;


public class Renderable extends Component {
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
	public final TriConsumer<Entity, RenderWindow, World> renderFn;

	/**
	 * The height to render this at, renderables with higher height
	 * values render above those with lower.
	 */
	public final int height;

	/**
	 *
	 * @param includedViews Which views this renderable renders in
	 * @param height The height to render this at, renderables with higher height
	 *				 values render above those with lower.
	 * @param renderFn The function to call to render this entity
	 */
	public Renderable(Set<ViewType> includedViews, int height, TriConsumer<Entity, RenderWindow, World> renderFn) {
		this.includedViews = includedViews;
		this.renderFn = renderFn;
		this.height = height;
	}

	public String serialize() {
		return "";
	}
}
