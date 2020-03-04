package scc210game.game.systems;

import scc210game.engine.ecs.System;
import scc210game.engine.ecs.World;
import scc210game.game.Shaders;

import javax.annotation.Nonnull;
import java.time.Duration;

public class WaterShaderUpdaterSystem implements System {
    private float value = 0.0f;

    @Override
    public void run(@Nonnull World world, @Nonnull Duration timeDelta) {
        this.value += timeDelta.toMillis() / 4000f;
        this.value %= 0.6f;
        Shaders.water.setParameter("rot", this.value);
    }
}
