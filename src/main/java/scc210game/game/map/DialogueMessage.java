package scc210game.game.map;

public class DialogueMessage {

	// Enemy = 0, NPC = 1, Chest = 2, Boss = 3, FinalBoss = 4
	//Grass = 0, Water = 1, Fire = 2, Ice = 3
	public DialogueMessage(int entityType, int biome) {
		switch (entityType) {
			case 0: {
				enemyDialogue();
				break;
			}
			case 1: {
				npcDialogue();
				break;
			}
			case 2: {
				chestDialogue();
				break;
			}
			case 3: {
				bossDialogue();
				break;
			}
			case 4: {
				finalBossDialogue();
				break;
			}

		}
	}



		private void enemyDialogue() {


		}

		private void npcDialogue() {


		}

		private void chestDialogue() {

		}

		private void bossDialogue() {

		}

		private void finalBossDialogue() {

		}

}


