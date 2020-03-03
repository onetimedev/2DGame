package scc210game.engine.events;

import scc210game.engine.combat.Scoring;
import scc210game.engine.state.event.StateEvent;

public class LeaveCombatEvent extends StateEvent {

    public Scoring score;
    public int enemyId;

    public LeaveCombatEvent(Scoring score, int enemyId)
    {
        this.score = score;
        this.enemyId = enemyId;
    }
}
