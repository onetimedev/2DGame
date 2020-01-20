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
	public final TriConsumer<Entity, RenderWindow, World> renderFn;
	public final int depth;

	public Renderable(Set<ViewType> includedViews, int depth, TriConsumer<Entity, RenderWindow, World> renderFn) {
		this.includedViews = includedViews;
		this.renderFn = renderFn;
		this.depth = depth;
	}

	public String serialize() {
		return "";
	}
}
