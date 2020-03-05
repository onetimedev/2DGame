package scc210game.game.components;

import scc210game.engine.ecs.Component;

public class ControlLock extends Component {

    private boolean locked = false;
    public boolean isLocked()
    {
        return locked;
    }


    public void unlock()
    {
        this.locked = false;
    }

    public void lock()
    {
        this.locked = true;
    }
}
