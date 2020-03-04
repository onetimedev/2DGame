package scc210game.game.states;

import org.jsfml.system.Vector2i;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.events.LeaveCombatEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPush;
import scc210game.engine.state.trans.Transition;
import scc210game.game.events.DialogueCreateEvent;
import scc210game.game.map.DialogueMessage;
import scc210game.game.map.Map;
import scc210game.game.map.Player;
import scc210game.game.map.Tile;
import scc210game.game.spawners.*;
import scc210game.game.states.events.TriggerCombatEvent;
import scc210game.game.spawners.ui.EnterInventoryButtonSpawner;
import scc210game.game.states.events.EnterInventoryEvent;
import scc210game.game.states.events.EnterTwoInventoryEvent;

public class MainGameState extends BaseInGameState {
	static {
		register(MainGameState.class, (j) -> new MainGameState());
	}

	@Override
	public void onStart(World world) {
        world.entityBuilder().with(new MapSpawner()).build();
        world.entityBuilder().with(new PlayerSpawner()).build();
        world.entityBuilder().with(
                new EnterInventoryButtonSpawner(0, 0, 0.05f, 0.05f))
                .build();


        var mapEnt = world.applyQuery(Query.builder().require(Map.class).build()).findFirst().orElseThrow();
        var map = world.fetchComponent(mapEnt, Map.class);

        // Spawning of all Chests
        for (final Tile t : map.getChestTiles()) {
            world.entityBuilder().with(new ChestSpawner(t)).build();
        }

        // Spawning of all Enemies
        int id = 0;
        for (final Tile tile : map.getEnemyTiles()) {
            world.entityBuilder().with(new EnemySpawner(tile, 40, id)).build();
            id++;
        }

        for (final Tile tile : map.getNPCTiles()) {
            world.entityBuilder().with(new NPCSpawner(tile)).build();
        }

        int count = 0;
        for (final Vector2i[] v : map.getBossCoords()) {
            world.entityBuilder().with(new BossSpawner(v, count, map, 55, id)).build();
            count++;
            id++;
        }


				Vector2i[] fBossTiles = {new Vector2i(59,59), new Vector2i(60,59), new Vector2i(61,59),
				new Vector2i(59,60), new Vector2i(60,60), new Vector2i(61,60),
				new Vector2i(59, 61), new Vector2i(60, 61), new Vector2i(61, 61)};
				for (Vector2i v: fBossTiles) {
					map.getTile(v.x,v.y).setHasEnemy(true);
					map.getTile(v.x,v.y).setTexture("map/basalt.png");
				}
        world.entityBuilder().with(new FinalBossSpawner(20, id)).build();


				world.eventQueue.broadcast(new DialogueCreateEvent(new DialogueMessage().getIntroDialogue(),
						(e, w) -> System.out.println(""),
						(e, w) -> System.out.println("")));
	}


	@Override
	public Transition handleEvent(StateEvent evt, World world) {
		if (evt instanceof EnterInventoryEvent) {
			EnterInventoryEvent evt1 = (EnterInventoryEvent) evt;
			return new TransPush(new InventoryViewState(world, evt1.invEnt, evt1.inv, evt1.selectedItemEnt, evt1.selectedItemInventory));
		}

		if (evt instanceof EnterTwoInventoryEvent) {
			EnterTwoInventoryEvent evt1 = (EnterTwoInventoryEvent) evt;
			return new TransPush(new TwoInventoryViewState(world, evt1.inv0Ent, evt1.inv0, evt1.selectedItemEnt, evt1.selectedItemInventory, evt1.inv1Ent, evt1.inv1));
		}

		if(evt instanceof TriggerCombatEvent){
			TriggerCombatEvent evt1 = (TriggerCombatEvent) evt;
			return new TransPush(new CombatState(evt1.scores, evt1.textureName, evt1.weapon, evt1.background, evt1.enemyDamage, evt1.enemy, evt1.weaponDamage));
		}


		return super.handleEvent(evt, world);


	}


}
