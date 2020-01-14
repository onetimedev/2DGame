package scc210game.render;


import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import scc210game.ecs.Entity;
import scc210game.ecs.Query;
import scc210game.ecs.System;
import scc210game.ecs.World;
import scc210game.state.State;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.stream.Stream;


public class RenderSystem implements System {
	private Query q = Query.builder()
			.require(Renderable.class)
			.build();
	private RenderTarget renderTarget;
	private RenderStates renderState;

	public RenderSystem(RenderTarget rt, RenderStates rs) {
		renderTarget = rt;
		renderState = rs;
	}

	@Override
	public void run(@Nonnull World world, @Nonnull Class<? extends State> currentState, @Nonnull Duration timeDelta) {
		Stream<Entity> renderEntities = world.applyQuery(q);

		renderEntities.forEach(re -> {
			var r = world.fetchComponent(re, Renderable.class);
			r.d.draw(renderTarget, renderState);
		});
	}


}
