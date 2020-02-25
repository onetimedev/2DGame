package scc210game.game.map;

import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Component;

public class PlayerTexture extends Component {

	public Texture texture;
	public int speedMs;

	public PlayerTexture(Texture t, int spMs) {
		this.texture = t;
		this.speedMs = spMs;
	}


	@Override
	public String serialize() {
		return null;
	}

}
