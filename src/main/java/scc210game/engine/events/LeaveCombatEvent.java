package scc210game.engine.events;

import scc210game.engine.combat.Scoring;

public class LeaveCombatEvent extends Event {

    public Scoring score;
    public int winner;

    public LeaveCombatEvent(Scoring score, int winner)
    {
        
    }
}
