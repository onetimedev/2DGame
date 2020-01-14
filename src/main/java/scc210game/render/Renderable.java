package scc210game.render;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderWindow;
import scc210game.ecs.Component;
import scc210game.ecs.Entity;

import java.util.function.BiConsumer;

public class Renderable extends Component {
	public final BiConsumer<Entity, RenderWindow> draw;

	public Renderable(BiConsumer<Entity, RenderWindow> d) {
		this.draw = d;
	}




	public String serialize() {
		return "";
	}



}
