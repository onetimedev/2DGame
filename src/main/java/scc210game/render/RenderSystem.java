package scc210game.render;


import org.jsfml.graphics.RenderWindow;
import scc210game.ecs.Entity;
import scc210game.ecs.Query;
import scc210game.ecs.System;
import scc210game.ecs.World;
import scc210game.state.State;
import scc210game.utils.Tuple2;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Comparator;
import java.util.stream.Stream;


public class RenderSystem implements System {
	private Query q = Query.builder()
			.require(Renderable.class)
			.build();
	private RenderWindow renderWindow;



	public RenderSystem(RenderWindow rw) {
		renderWindow = rw;
	}

	@Override
	public void run(@Nonnull World world, @Nonnull Class<? extends State> currentState, @Nonnull Duration timeDelta) {
		Stream<Entity> renderEntities = world.applyQuery(q);
		java.lang.System.out.println("In Run of RenderSystem");
		// renderEntities Stream using map to result in changed values in stream from sort. Making a tuple out of renderEntity (l) and Renderable Component (r) and sorting by depth
		// For each tupleRenderEntity get the renderable component and call accept on its data passing in the tupleRenderEntity renderEntity (l)
		renderEntities.map(renderEntity -> new Tuple2<>(renderEntity,  world.fetchComponent(renderEntity, Renderable.class)))
		  .sorted(Comparator.comparing(tupleRenderEntity -> {
			return tupleRenderEntity.r.depthValue;  // .r refers to right handside in the tuple
		} )).forEach(tupleRE -> {
			java.lang.System.out.println("For Each");
			tupleRE.r.drawData.accept(tupleRE.l, renderWindow, world);
		});
	}
}
