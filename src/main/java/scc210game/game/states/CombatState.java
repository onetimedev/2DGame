package scc210game.game.states;

import scc210game.engine.combat.*;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.World;
import scc210game.engine.state.InputHandlingState;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.Transition;
import scc210game.game.components.CombatData;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatPlayerWeapon;
import scc210game.game.components.Item;
import scc210game.game.spawners.CombatSpawner;
import scc210game.game.spawners.CombatWeapon;
import scc210game.game.spawners.ui.CombatBackground;
import scc210game.game.spawners.ui.HealthBarSpawner;

public class CombatState extends BaseInGameState {

    public CombatData combatData;

    public CombatState(CombatData cd) {
        combatData = cd;
    }


    @Override
    public void onStart(World world) {
        world.activateCombat();

        world.entityBuilder().with(new CombatBackground(combatData.background)).build();

        world.entityBuilder().with(new CombatHealthBar(CombatUtils.PLAYER)).build();
        world.entityBuilder().with(new CombatHealthBar(CombatUtils.BOSS)).build();

        world.entityBuilder().with(new CombatSpawner(new SpriteType("water enemy", "./src/main/resources/Combat/Enlarged/Water-boss-Combat-Animation-LARGE.png", true, 1))).build();

        world.entityBuilder().with(new CombatSpawner(new SpriteType("player", "./src/main/resources/textures/player.png", false, 0))).build();
        world.entityBuilder().with(new CombatWeapon(false, world, 5, "./src/main/resources/textures/player_sword.png")).build();


        world.entityBuilder().with(new CombatSprite("./src/main/resources/Combat/Enlarged/Water-boss-Combat-Animation-LARGE.png")).build();
        world.entityBuilder().with(new Scoring(combatData.scores.getPlayerExperience(), combatData.scores.getPlayerAbsHealth(), combatData.scores.getEnemyAbsHealth())).build();
        world.entityBuilder().with(new CombatResources()).build();

        new EnemyController(world, CombatEnemy.class, CombatPlayerWeapon.class, 3).start();

    }


    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        return super.handleEvent(evt, world);
    }
}
