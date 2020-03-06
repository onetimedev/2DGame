package scc210game.engine.events;

import scc210game.engine.combat.Scoring;
import scc210game.engine.ecs.Entity;
import scc210game.engine.state.event.StateEvent;

public class LeaveCombatEvent extends StateEvent {

    public Scoring score;
    public Entity enemy;
    public boolean playerWins;

    public LeaveCombatEvent(Scoring score, Entity enemy, boolean playerWins)
    {
        this.score = score;
        this.enemy = enemy;
        this.playerWins = playerWins;
    }

}
