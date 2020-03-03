package scc210game.engine.events;

import scc210game.engine.combat.Scoring;
import scc210game.engine.ecs.Entity;
import scc210game.engine.state.event.StateEvent;

public class LeaveCombatEvent extends StateEvent {

    public Scoring score;
    public int enemyId;

    public Entity enemy;

    public LeaveCombatEvent(Scoring score, int enemyId, Entity enemy)
    {
        this.score = score;
        this.enemyId = enemyId;
        this.enemy = enemy;
    }
}
