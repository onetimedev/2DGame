package scc210game.game.components;

import scc210game.engine.combat.Scoring;
import scc210game.engine.ecs.Component;

public class CombatData extends Component {

	public Scoring scores;
	public int biomeNum;
	public int bossType;
	public TextureStorage weapon;
	public String background;
	public int enemyDamage;


	public CombatData(Scoring s, int bn, int bt, TextureStorage wp, String bg, int enDmg) {
		scores = s;
		biomeNum = bn;
		bossType = bt;
		weapon = wp.copy();
		background = bg;
		enemyDamage = enDmg;
	}


}
