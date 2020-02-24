package scc210game.game.map;

public class DialogueMessage {

	private String message = "";

	// Enemy = 0, NPC = 1, Chest = 2, Boss = 3, FinalBoss = 4
	//Grass = 0, Water = 1, Fire = 2, Ice = 3
	public DialogueMessage(int entityType, int biome) {
		switch (entityType) {
			case 0: {
				enemyDialogue(biome);
				break;
			}
			case 1: {
				npcDialogue(biome);
				break;
			}
			case 2: {
				chestDialogue();
				break;
			}
			case 3: {
				bossDialogue(biome);
				break;
			}
			case 4: {
				finalBossDialogue();
				break;
			}

		}
	}


		//Grass = 0, Water = 1, Fire = 2, Ice = 3
		private void enemyDialogue(int biome) {
			switch (biome) {
				case 0: {
					grassEnemyDialogue();
					break;
				}
				case 1: {
					waterEnemyDialogue();
					break;
				}
				case 2: {
					fireEnemyDialogue();
					break;
				}
				case 3: {
					iceEnemyDialogue();
					break;
				}
			}
		}


		private void grassEnemyDialogue() {

		}

		private void waterEnemyDialogue() {

		}

		private void fireEnemyDialogue() {

		}

		private void iceEnemyDialogue() {

		}


		//Grass = 0, Water = 1, Fire = 2, Ice = 3
		private void npcDialogue(int biome) {
			switch (biome) {
				case 0: {
					grassNPCDialogue();
					break;
				}
				case 1: {
					waterNPCDialogue();
					break;
				}
				case 2: {
					fireNPCDialogue();
					break;
				}
				case 3: {
					iceNPCDialogue();
					break;
				}
			}
		}

	private void iceNPCDialogue() {
	}

	private void fireNPCDialogue() {
	}

	private void waterNPCDialogue() {
	}

	private void grassNPCDialogue() {
	}


	private void chestDialogue() {

		}



		//Grass = 0, Water = 1, Fire = 2, Ice = 3
		private void bossDialogue(int biome) {
			switch (biome) {
				case 0: {
					grassBossDialogue();
					break;
				}
				case 1: {
					waterBossDialogue();
					break;
				}
				case 2: {
					fireBossDialogue();
					break;
				}
				case 3: {
					iceBossDialogue();
					break;
				}
			}
		}

	private void iceBossDialogue() {
		message = "No need to shiver because of the cold, the fear induced from the elemental will suffice!";
		message += "\nThis elemental seized the cooling temperatures and has made these ice shelves its home for millenia.";
		message += "\nIt has prevented the people who used to live around what was the lake from returning to their own lands.";
	}

	private void fireBossDialogue() {
		message = "Now confronted in the depths of the lava fields the towering elemental appeared more dangerous than any amount of lava!";
	}

	private void waterBossDialogue() {
		message = "Spending time on the beach now seems a perilous endeavour, the elemental would make sure of it!";
	}

	private void grassBossDialogue() {
		message = "Trying to enter the clearing presented an imposing sight, the forests quietened and now only the elemental ahead could be heard!";
	}


	private void finalBossDialogue() {

	}


	private int randomMessage(int numOfMsg) {
		int rng = (int) (Math.random() * numOfMsg + 1);
		return rng;
	}


	public String getMessage() {
		String fullMsg = message;
		fullMsg += "\n\n----- Press Q to ignore, ENTER to accept -----";
		return fullMsg;
	}
}


