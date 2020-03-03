package scc210game.game.map;

import java.util.ArrayList;

/**
 * Class holding all dialogue messages.
*/
public class DialogueMessage {

	private String message = "";

	public DialogueMessage() {

	}

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
			message += "\n\n----- Press Q to Ignore, ENTER to Fight -----";
		}


		private void grassEnemyDialogue() {
			ArrayList<String> possDialogs = new ArrayList<>();
			possDialogs.add("The shrowded figure remained disturbingly silent.");
			possDialogs.add("Yellow eyes were the only features that could be made out on the mysterious foe.");

			message += possDialogs.get(randomMessage(possDialogs.size()));
		}

		private void waterEnemyDialogue() {
			ArrayList<String> possDialogs = new ArrayList<>();
			possDialogs.add("The creature resembled the water that surrounded it, luckily it could be seen on the sand!");
			possDialogs.add("While water has been known to kill, this creature looks like it will make it as painful as possible.");
			possDialogs.add("Bright red eyes warned any travellers away, there was no mistaking a sign to leave!");

			message += possDialogs.get(randomMessage(possDialogs.size()));
		}

		private void fireEnemyDialogue() {
			ArrayList<String> possDialogs = new ArrayList<>();
			possDialogs.add("Looking as if it has just crawled from the depths of one of the many lava pools, surely it must be as dangerous.");
			possDialogs.add("The creature sounded as if it was on fire, unfortunately not like the gentle crackling of logs.");

			message += possDialogs.get(randomMessage(possDialogs.size()));
		}

		private void iceEnemyDialogue() {
			ArrayList<String> possDialogs = new ArrayList<>();
			possDialogs.add("Clearly a foe as old as the ice that surrounded it.");
			possDialogs.add("A hiss came from the creature, a warning to any overly brave travellers of what might be yet to come.");

			message += possDialogs.get(randomMessage(possDialogs.size()));
		}

		public String getIceBossDefeatDialogue() {
			String s = "The ice where the elemental had once stood groaned and shimmered, it was as if an entire area had just relaxed.\n" +
					"No longer being stressed by an elemental presence that had made such a beautiful place inhospitable.\n" +
					"Now communities will be able to return and live as they once did, it is much closer to being home again.";

			return s;
		}

	public String getFireBossDefeatDialogue() {
		String s = "Feeling comfortable in such an environment doesn’t seem possible, but at least now the elemental terror has been removed.\n" +
				"Leaving only the natural dangers plaguing a travellers path.\n" +
				"Another step towards reclaiming your home from these elementals that have driven so many away and decimated what once were thriving villages.";

		return s;
	}

	public String getGrassBossDefeatDialogue() {
		String s = "Once again the forests erupted with noise, it almost seemed as though the many creatures were sending their thanks.\n" +
				"Even the grass seemingly started to move with the wind, no longer being stopped by such a monster.\n" +
				"Removing such a terror and drain on the earth element can only help to restore your home to what it once was.";
		return s;
	}

	public String getWaterBossDefeatDialogue() {
		String s = "Hearing the small waves lap onto the sand provided much needed peace after the terror imposed by the elemental.\n" +
				"Now perhaps these idyllic sands will be inhabitable again by those who choose to return to their lives here.\n" +
				"Restoring the populations trust that these elementals are truly gone will be challenging however.";

		return s;
	}

	public String getFinalBossDefeatDialogue() {
		String s = "The gigantic elemental crashed to the ground. Immediately a change could be felt, this was nothing like the previous elementals.\n" +
				"Controlling the elements and taking the land and homes of generations was no more. No longer would your home be threatening or dangerous.\n" +
				"Now the communities and lives can be restored, with the trust that such elementals are gone. Time will tell how impactful the reign of these creatures has been.";

		return s;
	}

	public String getNoEquippedWeapon() {
		return "Please equip a weapon to fight with!\n\n----- Press Q or ENTER to Continue -----";
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
			message += "\n\n----- Press Q or ENTER to Continue -----";
		}

	private void iceNPCDialogue() {
		ArrayList<String> possDialogs = new ArrayList<>();
		possDialogs.add("Most people lost everything when the monsters came, but me, I wasn’t good at anything, but now I get paid a kings fee to hunt monsters." +
				"\n You look like you're back to take my job!");
		possDialogs.add("Have you seen my friend Joseph, he went to hunt some food but never came back, I hope he’s alright.");
		possDialogs.add("Its gotten colder since they arrived, we've known it like this since you left.");
		possDialogs.add("I don't dare go far, have you heard those creatures hissing?");

		message += possDialogs.get(randomMessage(possDialogs.size()));
	}

	private void fireNPCDialogue() {
		ArrayList<String> possDialogs = new ArrayList<>();
		possDialogs.add("I can’t believe our beloved king, sits in his high castle eating like a pig, while we are all starving and getting slaughtered by the monsters.");
		possDialogs.add("I’ve heard that these monsters are from hell and the devil has come up from the underworld to punish us all.");
		possDialogs.add("It's a dangerous place sure, still be better of without these monsters!");
		possDialogs.add("They've made it hotter than ever, I don't know how much more I can take. But at least you're back.");

		message += possDialogs.get(randomMessage(possDialogs.size()));
	}

	private void waterNPCDialogue() {
		ArrayList<String> possDialogs = new ArrayList<>();
		possDialogs.add("Before all these monsters plagued our home, I had everything, my own shop, house, family, but now I have nothing.");
		possDialogs.add("This place used to be so beautiful, now look at it. Its changed so much since you've been away.");
		possDialogs.add("I'd never go in the water, not now.");
		possDialogs.add("I used to love swimming, i'm glad you're back to stop them!");
		possDialogs.add("Spending time on the beach now seems a perilous endeavour!");

		message += possDialogs.get(randomMessage(possDialogs.size()));
	}

	private void grassNPCDialogue() {
		ArrayList<String> possDialogs = new ArrayList<>();
		possDialogs.add("I lost my wife and son to these hideous monsters. Please avenge them.");
		possDialogs.add("I really hope our kings saves us soon! Have they sent you!");
		possDialogs.add("I have a theory that someone is controlling all these monsters, there’s no way they can all be this coordinated without a leader.");
		possDialogs.add("You can be the one to save us, we've needed you ever since you left!");

		message += possDialogs.get(randomMessage(possDialogs.size()));
	}


	private void chestDialogue() {
		ArrayList<String> possDialogs = new ArrayList<>();
		possDialogs.add("What could be inside?");
		possDialogs.add("It doesn't appear to be locked, i'm sure no one will notice the contents missing.");
		possDialogs.add("It might take some force to keep such a chest open.");
		possDialogs.add("Such beautiful craftsmanship, hopefully whatever is inside shares this quality.");
		possDialogs.add("Untouched, although these creatures don't have much dexterity.");

		message += possDialogs.get(randomMessage(possDialogs.size()));
		message += "\n\n----- Press Q to Ignore, ENTER to Open -----";

	}


	public String getVictoryDialogue() {
		String s = "";
		ArrayList<String> possDialogs = new ArrayList<>();
		possDialogs.add("A hard fought battle, through skill you have succeeded!");
		possDialogs.add("Another creature that can no longer plague your home!");
		possDialogs.add("A convincing victory!");

		s += possDialogs.get(randomMessage(possDialogs.size()));
		s += "\n\n----- Press Q or ENTER to Continue -----";
		return s;
	}


	public String getDefeatDialogue() {
		String s = "";
		ArrayList<String> possDialogs = new ArrayList<>();
		possDialogs.add("The light begins to fade, perhaps they are too strong.");
		possDialogs.add("It seems only the best weapons will suffice to defeat such a foe!");

		s += possDialogs.get(randomMessage(possDialogs.size()));
		s += "\n\n----- Press Q or ENTER to Respawn -----";
		return s;
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
			message += "\n\n----- Press Q to Ignore, ENTER to Fight -----";
		}

	private void iceBossDialogue() {
		message = "No need to shiver because of the cold, the fear induced from the elemental will suffice!";
		message += "\nThis elemental seized the cooling temperatures and has made these ice shelves its home for millenia.";
		message += "\nIt has prevented the people who used to live around what was the lake from returning to their own lands.";
	}

	private void fireBossDialogue() {
		message = "Now confronted in the depths of the lava fields the towering elemental appeared more dangerous than any amount of lava!";
		message += "\nWhile this area has never been thriving this elemental has made it impossible to even travel through!";
		message += "\nTo restore safe routes between the areas is vital to help the people who also used to live here to return.";
	}

	private void waterBossDialogue() {
		message = "Claiming the island as its own the elemental stood surrounded by sand, the homes that previously stood here no doubt beneath it.";
		message += "\nThe fish could no longer be seen in the water, nothing lived here now, nothing was misguided enough to believe it stood a chance.";
		message += "\nAll of the beautiful scenery was drowned out by the elemental, its presence had removed any allure from the water and sands.";
	}

	private void grassBossDialogue() {
		message = "Trying to enter the clearing presented an imposing sight, the forests quietened and now only the elemental ahead could be heard!";
		message += "\nThese beautiful fields and forests now lay empty, devoid of the community and happiness which they had once held.";
		message += "\n Nothing has been done by the Kings that govern this land, leaving such an elemental to be faced by the only guardian!";
	}

	private void finalBossDialogue() {
		message = "The largest elemental yet, it dwarfed the previous elementals fought and looked as if it was about to make sure their defeats would be avenged.";
		message += "\nThe hard rock surrounding it was surely a sign of what was to come for all areas if it is allowed to continue its terror.";
		message += "\nThis was the creature that had brought on such fear, and that had taken over the homes of so many.";
		message += "\nIts many skulls could be a sign of your fate, but who else has the ability to defeat such an elemental!";

		message += "\n\n----- Press Q to Ignore, ENTER to Fight -----";
	}


	public String getIntroDialogue() {
		String s = "After many years you have returned home, to revisit the communities and areas where you once lived.\n" +
				"However no longer present are those who you once knew! Now the inhabitants of your home are elemental creatures, intent on controlling the elements at any cost!\n" +
				"Move using W A S D    |    Enter or Q for dialogs    |    SPACE to attack    |    ESC to pause    |    ";

		return s;
	}

	private int randomMessage(int numOfMsg) {
		int rng = (int) (Math.random() * numOfMsg);
		return rng;
	}


	public String getMessage() {
		String fullMsg = message;
		return fullMsg;
	}
}


