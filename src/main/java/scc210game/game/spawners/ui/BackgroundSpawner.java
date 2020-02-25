package scc210game.game.spawners.ui;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.ResourceLoader;
import scc210game.engine.utils.UiUtils;

import java.io.IOException;
import java.util.Set;

public class BackgroundSpawner implements Spawner {
	private final Texture t = new Texture();
	private final Sprite bg = new Sprite();


	public BackgroundSpawner(String bgName) {
		try {
			t.loadFromFile(ResourceLoader.resolve("textures/backgrounds/" + bgName));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
		return builder
				.with(new UITransform(0, 0, 0, 1, 1))
				.with(new Renderable(Set.of(ViewType.UI), 0, (Entity e, RenderWindow rw, World w) -> {

					this.bg.setTexture(t);
					var position = UiUtils.convertUiPosition(rw, new Vector2f(0,0));
					this.bg.setPosition(position.x, position.y);
					//this.bg.setScale(new Vector2f(1.8f,2f));
					rw.draw(this.bg);

				}));
	}


}
