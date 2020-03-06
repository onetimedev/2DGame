package scc210game.game.components;

import scc210game.engine.ecs.Component;

import java.util.Random;

public class TargetPosition extends Component {

    public float xPos;
    public float yPos;
    public boolean visible = false;
    public long visibleUntil;
    public float offset = 0.0f;


    public static final int TIMEOUT = 500;

}
