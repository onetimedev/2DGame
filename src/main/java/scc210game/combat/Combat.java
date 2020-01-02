package scc210game.combat;

/**
 * Class that facilitates turn-based combat by updating
 * and managing the status of each combatant
 */
public class Combat {

	private CombatData combatData;
	private int turnNumber = 0;


	public Combat(CombatData data) {
		combatData = data;
	}


	/**
	 * Loop to continue the fight until there is a winner or it is deemed
	 * un-winnable by both combatants
	 */
	public void combatLoop() {
		while(combatData.getEnemyHealth() > 0 && combatData.getPlayerHealth() > 0) {
			if(turnNumber % 2 == 0) {
				// Player starts and the every even turn
			}
			else {
				// Enemy on every odd turn
			}

		turnNumber++;
		}

	}


	/**
	 * Check if a players item is not on cooldown
	 * @param item the item's cooldown to be checked
	 * @return true if the item can be used this turn, false if not
	 */
	public Boolean itemUsable(Item item) {
		//if(item.coolDown > 0)
			//return false;
		//else
			//return true;
	}


	/**
	 * Update player data, health, items, cool downs etc
	 */
	public void updatePlayerData() {
	}

	/**
	 * Update enemy data, health, cool downs etc
	 */
	public void updateEnemyData() {
	}

	/**
	 *  Needs to prevent infinite fights where neither party is doing enough damage each turn to win?
	 * @return true if there will be a winner, false if not
	 */
	public Boolean combatWinnable() {
	}



}
