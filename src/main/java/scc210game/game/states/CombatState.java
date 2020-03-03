package scc210game.game.states;

import scc210game.engine.combat.*;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
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
import scc210game.game.spawners.EnemySpawner;
import scc210game.game.spawners.ui.CombatBackground;

public class CombatState extends BaseInGameState {

    public Scoring scores;
    public String textureName;
    public TextureStorage weapon;
    public String background;
    public int enemyDamage;
    public Entity enemy;

    public CombatState(Scoring s, String tn, TextureStorage wp, String bg, int enDmg, Entity enemy) {
        scores = s;
        textureName = tn;
        weapon = wp.copy();
        background = bg;
        enemyDamage = enDmg;
        this.enemy = enemy;
        System.out.println(enemyDamage);
    }


    @Override
    public void onStart(World world) {
        world.activateCombat();

        world.entityBuilder().with(new CombatInfo()).build();
        world.entityBuilder().with(new CombatBackground(background)).build();
        //TODO:
        world.entityBuilder().with(new CombatHealthBar(CombatUtils.PLAYER)).build();
        world.entityBuilder().with(new CombatHealthBar(CombatUtils.BOSS)).build();

        world.entityBuilder().with(new CombatSpawner(new SpriteType("water enemy", textureName, true, 1))).build();

        world.entityBuilder().with(new CombatSpawner(new SpriteType("player", CombatUtils.PLAYER_SPRITE, false, 0))).build();
        world.entityBuilder().with(new CombatWeapon(false, world, 5, weapon.getPath())).build();


        world.entityBuilder().with(new CombatSprite(textureName)).build();
        world.entityBuilder().with(new Scoring(scores.getPlayerExperience(), scores.getPlayerAbsHealth(),scores.getEnemyAbsHealth())).build();
        world.entityBuilder().with(new CombatResources()).build();

        world.entityBuilder().with(new EnemyController(world, CombatEnemy.class, enemyDamage)).build();


    }


    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if(evt instanceof ExitCombatState)
        {
            var scoring = world.applyQuery(Query.builder().require(Scoring.class).build()).findFirst().orElseThrow();
            var combatInfo = world.applyQuery(Query.builder().require(CombatInfo.class).build()).findFirst().orElseThrow();

            var scores = world.fetchComponent(scoring, Scoring.class);
            var info = world.fetchComponent(combatInfo, CombatInfo.class);
            world.ecs.eventQueue.broadcast(new LeaveCombatEvent(new Scoring(scores.playerExperience,scores.getPlayerAbsHealth(),scores.getEnemyAbsHealth()), this.enemy, info.didPlayerWin));  //TODO:
            return TransPop.getInstance();
        }
        return super.handleEvent(evt, world);
    }
}
