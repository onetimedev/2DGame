package scc210game.state.event;

import scc210game.events.Event;

/**
 * Interface that marks a state event type.
 * State events are those that are passed from the window to the current state
 * The current state then decides what to do with the event
 * <p>
 * For raw input events ({@link InputEvent}), these will probably all be forwarded to the event queue for the interaction handler
 * system to interpret into {@link scc210game.events.UiEvent} events.
 */
public abstract class StateEvent extends Event {
}
