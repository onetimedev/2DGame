package scc210game.game.components;

import scc210game.engine.ecs.Component;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Represents objects that have popup hover info
 */
public class HasToolTip extends Component {
    @Nonnull public HoverState hoverState;

    public HasToolTip() {
        hoverState = HoverState.NOTHOVERED;
    }

    private static final List<HoverState> visibleStates = List.of(
        HoverState.ENTERING,
        HoverState.HOVERED,
        HoverState.LEAVING
    );

    public boolean isVisible() {
        return visibleStates.contains(this.hoverState);
    }

    public float calcOpacity(float animPct) {
        switch (this.hoverState) {
            case NOTHOVERED:
                return 0.0f;
            case ENTERING:
                return animPct;
            case HOVERED:
                return 1.0f;
            case LEAVING:
                return 1.0f - animPct;
        }

        // unreachable, java just can't figure it out
        return 0.0f;
    }

    @Override
    public String serialize() {
        return null;
    }

     public enum HoverState {
         NOTHOVERED,
         ENTERING,
         HOVERED,
         LEAVING
     }

    @Override
    public String toString() {
        return "HasToolTip{" +
                "hoverState=" + hoverState +
                '}';
    }
}
