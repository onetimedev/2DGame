package scc210game.game.components;

import scc210game.engine.ecs.Component;

/**
 * Represents objects that have popup hover info
 */
public class HoverInfo extends Component {
    public final String text;

    public HoverInfoState hoverState;

    public HoverInfo(String text) {
        this.text = text;
    }

    @Override
    public String serialize() {
        return null;
    }

     public enum HoverInfoState {
         NOTHOVERED,
         ENTERING,
         HOVERED,
         LEAVING
     }
}
