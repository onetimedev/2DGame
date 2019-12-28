package scc210game.state;

import scc210game.state.event.StateEvent;
import scc210game.state.trans.Transition;

public interface State {
    /**
     * Called when this game state is entered
     */
    void onStart();

    /**
     * Called when this game state is stopped
     */
    void onStop();

    /**
     * Called when another game state is put on top of this state
     */
    void onPause();

    /**
     * Called when this game state is unpaused
     */
    void onResume();

    /**
     * Called when this state needs to handle an event
     *
     * @param evt the Event to handle
     * @return a Transition object describing what to do
     */
    Transition handleEvent(StateEvent evt);

    /**
     * Called as often as possible
     *
     * @return a Transition object describing what to do
     */
    Transition update();

    /**
     * Called every frame
     *
     * @return a Transition object describing what to do
     */
    Transition onFrame();
}
