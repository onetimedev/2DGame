package scc210game.game.states.events;

import scc210game.engine.combat.Scoring;
import scc210game.engine.ecs.Entity;
import scc210game.engine.state.event.StateEvent;
import scc210game.game.components.TextureStorage;

public class TriggerCombatEvent extends StateEvent {

	public Scoring scores;
	public String textureName;
	public TextureStorage weapon;
	public String background;
	public int enemyDamage;
	public Entity enemy;

	public TriggerCombatEvent(Scoring s, String tn, TextureStorage wp, String bg, int enDmg, Entity enemy) {
		scores = s;
		textureName = tn;
		weapon = wp.copy();
		background = bg;
		enemyDamage = enDmg;
		this.enemy = enemy;
	}

}
