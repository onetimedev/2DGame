package scc210game.render;


import org.jsfml.graphics.RenderWindow;
import scc210game.ecs.Component;
import scc210game.ecs.Entity;
import scc210game.ecs.World;
import scc210game.utils.TriConsumer;


public class Renderable extends Component {
	public final TriConsumer<Entity, RenderWindow, World> drawData;
	public final int depthValue;

	public Renderable(TriConsumer<Entity, RenderWindow, World> d, int depth) {
		this.drawData = d;
		this.depthValue = depth;
	}

	public String serialize() {
		return "";
	}



}
