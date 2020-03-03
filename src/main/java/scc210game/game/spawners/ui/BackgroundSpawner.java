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
import scc210game.game.components.TextureStorage;

import java.io.IOException;
import java.util.Set;

public class BackgroundSpawner implements Spawner {
	private final String backgroundName;


	public BackgroundSpawner(String bgName) {
		backgroundName = bgName;
	}


	@Override
	public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
		return builder
				.with(new UITransform(0, 0, 0, 1, 1))
				.with(new TextureStorage("textures/backgrounds/" + backgroundName))
				.with(new Renderable(Set.of(ViewType.UI), 0, BackgroundSpawner::accept));
	}


	private static void accept(Entity e, RenderWindow rw, World w) {

		var bgTexture = w.fetchComponent(e, TextureStorage.class);

		var position = UiUtils.convertUiPosition(rw, new Vector2f(0,0));
		var bg = new Sprite(bgTexture.getTexture());

		bg.setPosition(position.x, position.y);

		rw.draw(bg);
	}


}
