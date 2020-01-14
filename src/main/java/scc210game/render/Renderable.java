package scc210game.render;

import org.jsfml.graphics.Drawable;
import scc210game.ecs.Component;

public class Renderable extends Component {
	public final Drawable d;

	public Renderable(Drawable d) {
		this.d = d;
	}




	public String serialize() {
		return "";
	}



}
