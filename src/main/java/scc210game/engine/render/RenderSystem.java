package scc210game.engine.render;


import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.utils.Tuple2;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


@SuppressWarnings("BoundedWildcard")
public class RenderSystem implements System {
    private final RenderWindow renderWindow;
    private final Map<ViewType, View> views;
    private final List<ViewType> viewOrder;
    private final Query q = Query.builder()
            .require(Renderable.class)
            .build();

    public RenderSystem(RenderWindow rw, Map<ViewType, View> views, List<ViewType> viewOrder) {
        this.renderWindow = rw;
        this.views = views;
        this.viewOrder = viewOrder;
    }

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        for (final ViewType viewType : this.viewOrder) {
            var view = this.views.get(viewType);
            Stream<Entity> renderEntities = world.applyQuery(this.q);
            // renderEntities Stream using map to result in changed values in stream from sort.
            // Making a tuple out of renderEntity (l) and Renderable Component (r) and sorting by depth
            // For each tupleRenderEntity get the renderable component and call accept
            // on its data passing in the tupleRenderEntity renderEntity (l)

            renderEntities
                    .map(e -> new Tuple2<>(e, world.fetchComponent(e, Renderable.class)))
                    .filter(t -> t.r.includedViews.contains(viewType))
                    .sorted(Comparator.comparing(t -> t.r.height))
                    .forEach(t -> {
                        this.renderWindow.setView(view);
                        t.r.renderFn.accept(t.l, this.renderWindow, world);
                    });
        }
    }
}
