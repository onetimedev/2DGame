package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Represents objects that have popup hover info
 */
public class HasToolTip extends Component {
    @Nonnull public HoverState hoverState;

    public HasToolTip() {
        hoverState = HoverState.NOTHOVERED;
    }

    HasToolTip(@Nonnull HoverState hoverState) {
        this.hoverState = hoverState;
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

    static {
        register(HasToolTip.class, j -> {
            var json = (JsonObject) j;
            var stateS = (String) json.get("state");
            HoverState state;
            switch (stateS) {
                case "NOTHOVERED":
                    state = HoverState.NOTHOVERED;
                    break;
                case "ENTERING":
                    state = HoverState.ENTERING;
                    break;
                case "HOVERED":
                    state = HoverState.HOVERED;
                    break;
                case "LEAVING":
                    state = HoverState.LEAVING;
                    break;
                default:
                    throw new RuntimeException("Impossible");
            }
            return new HasToolTip(state);
        });
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of("state", this.hoverState.toString()));
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
