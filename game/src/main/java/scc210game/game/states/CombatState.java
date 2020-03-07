package scc210game.game.states;

import scc210game.engine.state.trans.TransNop;
import scc210game.game.combat.*;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.events.ExitCombatState;
import scc210game.engine.events.LeaveCombatEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPop;
import scc210game.engine.state.trans.Transition;
import scc210game.game.components.*;
import scc210game.game.resources.MainWorldEventQueueResource;
import scc210game.game.spawners.*;
import scc210game.game.spawners.ui.CombatBackground;
import scc210game.game.states.events.TogglePauseEvent;

public class CombatState extends BaseInGameState {

    public Scoring scores;
    public String textureName;
    public TextureStorage weapon;
    public String background;
    public int enemyDamage;
    public Entity enemy;
    public int weaponDamage;


    public CombatState(Scoring s, String tn, TextureStorage wp, String bg, int enDmg, Entity enemy, int weaponDamage) {
        scores = s;
        textureName = tn;
        weapon = wp.copy();
        background = bg;
        enemyDamage = enDmg;
        this.enemy = enemy;
        this.weaponDamage = weaponDamage;
    }


    @Override
    public void onStart(World world) {
        String weaponPath = CombatUtils.RES_ROOT_PATH + weapon.getPath();

        world.addResource(new CombatResources());

        var combatResource = CombatUtils.getCombatResources(world);
        combatResource.isCombatActive = true;

        world.entityBuilder().with(new TargetPosition()).build();
        world.entityBuilder().with(new CombatInfo()).build();
        world.entityBuilder().with(new ControlLock()).build();
        world.entityBuilder().with(new CombatDialog()).build();

        world.entityBuilder().with(new CombatBackground(background)).build();
        world.entityBuilder().with(new CombatDialogSpawner(world)).build();

        world.entityBuilder().with(new CombatHealthBar(CombatUtils.PLAYER)).build();
        world.entityBuilder().with(new CombatHealthBar(CombatUtils.BOSS)).build();

        world.entityBuilder().with(new CombatSpawner(new SpriteType("water enemy", textureName, true, enemyDamage), world)).build();

        world.entityBuilder().with(new CombatSpawner(new SpriteType("player", CombatUtils.PLAYER_SPRITE, false, 0), world)).build();
        world.entityBuilder().with(new CombatWeapon(false, world, weaponDamage, weaponPath)).build();

        world.entityBuilder().with(new CombatSprite(textureName)).build();
        world.entityBuilder().with(new Scoring(scores.getPlayerExperience(), scores.getPlayerAbsHealth() ,scores.getEnemyAbsHealth())).build();


        world.entityBuilder().with(new EnemyController(world, CombatEnemy.class, enemyDamage)).build();

        world.entityBuilder().with(new WoundSpawner()).build();


    }


    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if(evt instanceof ExitCombatState)
        {
            var scoring = world.applyQuery(Query.builder().require(Scoring.class).build()).findFirst().orElseThrow();
            var combatInfo = world.applyQuery(Query.builder().require(CombatInfo.class).build()).findFirst().orElseThrow();

            var scores = world.fetchComponent(scoring, Scoring.class);
            var info = world.fetchComponent(combatInfo, CombatInfo.class);
            var mainWorldEventQ = world.fetchGlobalResource(MainWorldEventQueueResource.class);
            mainWorldEventQ.queue.broadcast(new LeaveCombatEvent(new Scoring(scores.playerExperience + 5,scores.getPlayerAbsHealth(),scores.getEnemyAbsHealth()), this.enemy, info.didPlayerWin));  //TODO:
            return TransPop.getInstance();
        }

        // don't pause in combat
        if (evt instanceof TogglePauseEvent) {
            return TransNop.getInstance();
        }

        return super.handleEvent(evt, world);
    }
}
