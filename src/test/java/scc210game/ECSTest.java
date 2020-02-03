package scc210game;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.junit.Test;
import scc210game.engine.ecs.System;
import scc210game.engine.ecs.*;
import scc210game.engine.state.State;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
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
}

class Velocity extends Component {
    static {
        register(Velocity.class, s -> {
            final JsonObject json = Jsoner.deserialize(s, new JsonObject());

            BigDecimal dx = (BigDecimal) json.get("dx");
            BigDecimal dy = (BigDecimal) json.get("dy");

            return new Velocity(dx.intValue(), dy.intValue());
        });
    }

    public final int dx;
    public final int dy;

    public Velocity(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public String serialize() {
        final Jsonable json = new JsonObject() {{
            this.put("dx", Velocity.this.dx);
            this.put("dy", Velocity.this.dy);
        }};

        return json.toJson();
    }
}

class Position extends Component {
    static {
        register(Position.class, s -> {
            final JsonObject json = Jsoner.deserialize(s, new JsonObject());

            BigDecimal x = (BigDecimal) json.get("x");
            BigDecimal y = (BigDecimal) json.get("y");

            return new Position(x.intValue(), y.intValue());
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
    public String serialize() {
        final Jsonable json = new JsonObject() {{
            this.put("x", Position.this.x);
            this.put("y", Position.this.y);
        }};

        return json.toJson();
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
                    world.resetModifiedState(e);
                    this.actOnEntity(e, world, timeDelta);
                });
            }

            public void actOnEntity(Entity e, @Nonnull World world, @Nonnull Duration timeDelta) {
                var pos = world.fetchComponent(e, Position.class);
                var vel = world.fetchComponent(e, Velocity.class);

                pos.x += vel.dx;
                pos.y += vel.dy;

                world.setModified(e, Position.class);
            }
        }

        var s = new BasicState();

        ECS ecs = new ECS(List.of(new System0()), s);

        ecs.start();

        ecs.runOnce();

        var entPos = ecs.getCurrentWorld().fetchComponent(s.e, Position.class);

        assertEquals(entPos.x, 1);

        Position testPos = new Position(100, -100);

        assertEquals(testPos, Component.deserialize(testPos.serialize(), Position.class));
    }
}
