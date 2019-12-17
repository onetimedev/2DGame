package scc210game;

import org.junit.Test;
import scc210game.ecs.System;
import scc210game.ecs.*;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ECSTest {
    @Test
    public void testECS() {
        class Velocity implements Component {
            public final int dx;
            public final int dy;

            public Velocity(int dx, int dy) {
                this.dx = dx;
                this.dy = dy;
            }
        }

        class Position implements Component {
            public int x, y;

            public Position(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }
        class System0 extends System {
            @Nonnull
            @Override
            public Query getQuery() {
                return Query.builder()
                        .require(Position.class)
                        .require(Velocity.class)
                        .build();
            }

            @Override
            public void actOnEntity(Entity e, @Nonnull World world, @Nonnull Duration timeDelta) {
                var pos = world.fetchComponent(e, Position.class);
                var vel = world.fetchComponent(e, Velocity.class);

                pos.x += vel.dx;
                pos.y += vel.dy;

                world.setModified(e, Position.class);
            }
        }

        ECS ecs = new ECS(List.of(new System0()));

        var ent = ecs.entityBuilder()
                .with(new Position(0, 0))
                .with(new Velocity(1, 0))
                .build();

        ecs.runOnce();

        var entPos = ecs.fetchComponent(ent, Position.class);

        assertEquals(entPos.x, 1);
    }
}
