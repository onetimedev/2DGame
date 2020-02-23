package scc210game.engine.animation;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.utils.SerializableBiConsumer;
import scc210game.engine.utils.SerializeToBase64;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Map;

public class Animate extends Component {
    private final Duration duration;
    public float pctComplete;

    @Nonnull
    public SerializableBiConsumer<Entity, World> completionCallback;

    public Animate(Duration duration, @Nonnull SerializableBiConsumer<Entity, World> completionCallback) {
        this.duration = duration;
        this.completionCallback = completionCallback;
        this.pctComplete = 0.0f;
    }

    public void update(Duration td) {
        var pct = td.dividedBy(this.duration);
        this.pctComplete += pct;
    }

    public boolean isComplete() {
        return this.pctComplete >= 1.0f;
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of(
                "duration", this.duration,
                "pctComplete", this.pctComplete,
                "completionCallback", SerializeToBase64.serializeToBase64(this.completionCallback)));
    }
}
