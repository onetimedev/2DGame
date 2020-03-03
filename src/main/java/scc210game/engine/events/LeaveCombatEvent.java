package scc210game.engine.events;

import scc210game.engine.combat.Scoring;
import scc210game.engine.state.event.StateEvent;

public class LeaveCombatEvent extends StateEvent {

    public Scoring score;
    public int winner;

    public LeaveCombatEvent(Scoring score, int winner)
    {
        this.score = score;
        this.winner = winner;
    }
}
