package scc210game;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import org.junit.Test;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.*;
import scc210game.engine.state.State;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

class BasicState extends State {
    public Entity e;

    @Override
    public void onStart(World world) {
        this.e = world.entityBuilder()
                .with(new Position(0, 0))
                .with(new Velocity(1, 0))
                .build();
    }

    @Override
    public Jsonable serialize() {
        return null;
    }
}

class Velocity extends Component {
    static {
        register(Velocity.class, j -> {
            var json = (JsonObject) j;

            var dx = (Integer) json.get("dx");
            var dy = (Integer) json.get("dy");

            return new Velocity(dx, dy);
        });
    }

    public final int dx;
    public final int dy;

    public Velocity(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public Jsonable serialize() {
        final Jsonable json = new JsonObject() {{
            this.put("dx", Velocity.this.dx);
            this.put("dy", Velocity.this.dy);
        }};

        return json;
    }
}

class Position extends Component {
    static {
        register(Position.class, j -> {
            var json = (JsonObject) j;

            var x = (Integer) json.get("x");
            var y = (Integer) json.get("y");

            return new Position(x, y);
        });
    }

    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (this.x != position.x) return false;
        return this.y == position.y;
    }

    @Override
    public int hashCode() {
        int result = this.x;
        result = 31 * result + this.y;
        return result;
    }

    @Override
    public Jsonable serialize() {
        final Jsonable json = new JsonObject() {{
            this.put("x", Position.this.x);
            this.put("y", Position.this.y);
        }};

        return json;
    }
}

public class ECSTest {
    @Test
    public void testECS() {
        class System0 implements System {
            private final Query q = Query.builder()
                    .require(Position.class)
                    .require(Velocity.class)
                    .build();

            @Override
            public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
                final Stream<Entity> entities = world.applyQuery(this.q);

                entities.forEach(e -> {
                    this.actOnEntity(e, world, timeDelta);
                });
            }

            public void actOnEntity(Entity e, @Nonnull World world, @Nonnull Duration timeDelta) {
                var pos = world.fetchComponent(e, Position.class);
                var vel = world.fetchComponent(e, Velocity.class);

                pos.x += vel.dx;
                pos.y += vel.dy;
            }
        }

        var s = new BasicState();

        ECS ecs = new ECS(List.of((ecs_) -> new System0()), s);

        ecs.start();

        ecs.runOnce();

        var entPos = ecs.getCurrentWorld().fetchComponent(s.e, Position.class);

        assertEquals(entPos.x, 1);

        Position testPos = new Position(100, -100);

        assertEquals(testPos, Component.deserialize(testPos.serialize(), Position.class));

        ecs.getCurrentWorld().removeEntity(s.e);
        s.e = null;
    }
}
