package scc210game.combat;

import scc210game.ecs.Entity;

/**
 * 	Takes attributes from player and enemy to be tracked and modified during
 * 	combat. Subsequent values after combat are only returned once combat has finished
 */
public class CombatData {

	private int playerHealth;
	private int enemyHealth;
	// Item[] playerItems;
		// Need to hold cool down times for each item (int numberOfTurnsDisabled)
	// Player Sprite
	// Enemy Sprite
	// Tile Type/Biome -> UI Background
	// Enemy Type
	// Enemy Damage


	/**
	 * Constructor to map given player and enemy attributes to instance variables
	 * @param player the player entity
	 * @param enemy the enemy entity
	 * @param playerTile the current tile the player is on
	 */
	public CombatData(Entity player, Entity enemy, Tile playerTile) {


	}


	public int getPlayerHealth() {
		return playerHealth;
	}

	public int getEnemyHealth() {
		return enemyHealth;
	}



}
