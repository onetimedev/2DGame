package scc210game.engine.animation;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.function.BiConsumer;

public class Animate extends Component {
    private Duration duration;
    public float pctComplete;

    @Nonnull
    public BiConsumer<Entity, World> completionCallback;

    public Animate(Duration duration, @Nonnull BiConsumer<Entity, World> completionCallback) {
        this.duration = duration;
        this.completionCallback = completionCallback;
        this.pctComplete = 0.0f;
    }

    public Animate(Duration duration, float pctComplete, @Nonnull BiConsumer<Entity, World> completionCallback) {
        this.duration = duration;
        this.pctComplete = pctComplete;
        this.completionCallback = completionCallback;
    }

    public void update(Duration td) {
        var pct = (float) td.toNanos() / (float) this.duration.toNanos();
        this.pctComplete += pct;
    }

    public boolean isComplete() {
        return this.pctComplete >= 1.0f;
    }

    @Override
    public String serialize() {
        return null;
    }
}
