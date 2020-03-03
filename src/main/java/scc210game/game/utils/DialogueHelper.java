package scc210game.game.utils;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.render.MainViewResource;
import scc210game.game.components.PlayerLocked;

public class DialogueHelper {

	/**
	 * Method triggered upon player refusal of an entities dialogue option.
	 * @param world the world for this state
	 * @param player the player entity
	 */
	public static void refuse(World world, Entity player) {
		var view = world.fetchGlobalResource(MainViewResource.class);
		view.mainView.zoom(1f/0.6f);
		var positionLocked = world.fetchComponent(player, PlayerLocked.class);
		positionLocked.locked = false;
	}
}
