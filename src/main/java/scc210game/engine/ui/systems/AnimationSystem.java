package scc210game.engine.ui.systems;

import scc210game.engine.ecs.ECS;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.engine.events.EventQueueReader;
import scc210game.engine.ui.components.UITransform;

import javax.annotation.Nonnull;
import java.time.Duration;

public class AnimationSystem implements System {

    private final EventQueueReader eventQueueReader;

    Entity tbAnimated;
    World w;

    public AnimationSystem(ECS ecs){
        this.eventQueueReader = ecs.eventQueue.makeReader();
        ecs.eventQueue.listen(this.eventQueueReader, .class);

    }


    public void setTbAnimated(Entity e, World w){
        tbAnimated = e;
        this.w = w;
    }


    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {

        UITransform pAttributes = w.fetchComponent(tbAnimated, UITransform.class);
        pAttributes.xPos += 0.01f;


    }
}
