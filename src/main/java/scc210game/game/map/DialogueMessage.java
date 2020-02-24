package scc210game.game.map;

import java.util.ArrayList;

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
		ArrayList<String> possDialogs = new ArrayList<>();
		possDialogs.add("Most people lost everything when the monsters came, but me, I wasn’t good at anything, but now I get paid a kings fee to hunt monsters.");
		possDialogs.add("Have you seen my friend Joseph, he went to hunt some food but never came back, I hope he’s alright.");

		message += possDialogs.get(randomMessage(possDialogs.size()));
	}

	private void fireNPCDialogue() {
		ArrayList<String> possDialogs = new ArrayList<>();
		possDialogs.add("I can’t believe our beloved king, sits in his high castle eating like a pig, while we are all starving and getting slaughtered by the monsters.");
		"I’ve heard that these monsters are from hell and the devil has come up from the underworld to punish us all.");

		message += possDialogs.get(randomMessage(possDialogs.size()));
	}

	private void waterNPCDialogue() {
		ArrayList<String> possDialogs = new ArrayList<>();
		possDialogs.add("Before all these monsters plagued this land, I had everything, my own shop, house, family, but now I have nothing.");
		possDialogs.add("This place used to be so beautiful, now look at it.");

		message += possDialogs.get(randomMessage(possDialogs.size()));
	}

	private void grassNPCDialogue() {
		ArrayList<String> possDialogs = new ArrayList<>();
		possDialogs.add("I lost my wife and son to these hideous monsters");
		possDialogs.add("I really hope our kings saves us soon!");
		possDialogs.add("I have a theory that someone is controlling all these monsters, there’s no way they can all be this coordinated without a leader.");

		message += possDialogs.get(randomMessage(possDialogs.size()));
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


