package scc210game.game.states;

import scc210game.engine.combat.*;
import scc210game.engine.ecs.World;
import scc210game.engine.events.ExitCombatState;
import scc210game.engine.events.LeaveCombatEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPop;
import scc210game.engine.state.trans.Transition;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatPlayerWeapon;
import scc210game.game.components.TextureStorage;
import scc210game.game.spawners.CombatSpawner;
import scc210game.game.spawners.CombatWeapon;
import scc210game.game.spawners.ui.CombatBackground;

public class CombatState extends BaseInGameState {

    public Scoring scores;
    public String textureName;
    public TextureStorage weapon;
    public String background;
    public int enemyDamage;

    public CombatState(Scoring s, String tn, TextureStorage wp, String bg, int enDmg) {
        scores = s;
        textureName = tn;
        weapon = wp.copy();
        background = bg;
        enemyDamage = enDmg;
    }


    @Override
    public void onStart(World world) {
        world.activateCombat();


        world.entityBuilder().with(new CombatBackground(background)).build();
        //TODO:
        world.entityBuilder().with(new CombatHealthBar(CombatUtils.PLAYER)).build();
        world.entityBuilder().with(new CombatHealthBar(CombatUtils.BOSS)).build();

        world.entityBuilder().with(new CombatSpawner(new SpriteType("water enemy", textureName, true, 1))).build();

        world.entityBuilder().with(new CombatSpawner(new SpriteType("player", "src/main/resources/textures/Combat/Player-in-combat.png", false, 0))).build();
        world.entityBuilder().with(new CombatWeapon(false, world, 5, "src/main/resources/textures/Weapons/Basic-Sword.png")).build();


        world.entityBuilder().with(new CombatSprite(textureName)).build();
        world.entityBuilder().with(new Scoring(scores.getPlayerExperience(), scores.getPlayerAbsHealth(),scores.getEnemyAbsHealth())).build();
        world.entityBuilder().with(new CombatResources()).build();

        new EnemyController(world, CombatEnemy.class, CombatPlayerWeapon.class, 3).start();

    }


    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if(evt instanceof ExitCombatState)
        {
            world.ecs.eventQueue.broadcast(new LeaveCombatEvent(new Scoring(0,0,0), 0));  //TODO:
            return TransPop.getInstance();
        }
        return super.handleEvent(evt, world);
    }
}
