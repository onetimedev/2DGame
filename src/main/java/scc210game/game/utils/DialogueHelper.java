package scc210game.game.utils;

import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.render.MainViewResource;
import scc210game.game.components.PlayerLocked;
import scc210game.game.resources.ZoomStateResource;

public class DialogueHelper {

	/**
	 * Method triggered upon player refusal of an entities dialogue option.
	 * @param world the world for this state
	 * @param player the player entity
	 */
	public static void refuse(World world, Entity player) {
		var view = world.fetchGlobalResource(MainViewResource.class);
		view.zoomOut();
		var zoomState = world.fetchGlobalResource(ZoomStateResource.class);
		zoomState.zoomed = false;
		var positionLocked = world.fetchComponent(player, PlayerLocked.class);
		positionLocked.locked = false;
	}
}
