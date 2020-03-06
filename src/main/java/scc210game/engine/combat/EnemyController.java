package scc210game.engine.combat;

import org.jsfml.graphics.Texture;
import scc210game.engine.audio.Audio;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.ResourceLoader;
import scc210game.game.components.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EnemyController extends Component{

    private World w;
    private Class<? extends Component> spriteClass;
    private boolean start = true;
    private boolean fight = true;

    private ScheduledExecutorService scheduledExecutorService;

    private float WEAPON_RAISED = 10f;
    private float WEAPON_HOLSTERED = 0f;

    private int collisionCount = 0;

    private int damage;
    Audio au = new Audio();



    public EnemyController(World w, Class<? extends Component> spriteClass, int damage){
        this.w = w;
        this.spriteClass = spriteClass;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.damage = damage;
        start();
    }

    private void start()
    {

        scheduledExecutorService.scheduleAtFixedRate(this::initMove, 0, 800, TimeUnit.MILLISECONDS);

    }

    private int getHealth()
    {
        return new CombatUtils().getAbsHealth(w, true);

    }


    public int maxFrame()
    {
        try{
            Texture t = new Texture();
            t.loadFromFile(Paths.get(getSprite().spriteImage));
            return (t.getSize().x / t.getSize().y)-1;
        }catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }

    private void animateSprite()
    {
        getSprite().nextChange = System.currentTimeMillis() + 60;

        if(getSprite().enemyState < maxFrame()) {
            getSprite().enemyState++;
        }
        getSprite().signal = true;
    }




    private CombatSprite getSprite()
    {
        var spriteState = w.applyQuery(Query.builder().require(CombatSprite.class).build()).findFirst().orElseThrow();
        return w.fetchComponent(spriteState, CombatSprite.class);
    }


    private void initMove()
    {

        var spriteState = w.applyQuery(Query.builder().require(CombatSprite.class).build()).findFirst().orElseThrow();
        var state = w.fetchComponent(spriteState, CombatSprite.class);

        var cLock = w.applyQuery(Query.builder().require(ControlLock.class).build()).findFirst().orElseThrow();
        var lock = w.fetchComponent(cLock, ControlLock.class);

        var cDialog = w.applyQuery(Query.builder().require(CombatDialog.class).build()).findFirst().get();
        var dialog = w.fetchComponent(cDialog, CombatDialog.class);

        if(w.getCombatStatus())
        {
            if(getHealth() > 0)
            {

                if (collisionCount >= getCollisionMax())
                {
                    new CombatAnimator(w, CombatPlayer.class, CombatPlayerWeapon.class, 65, CombatUtils.BACKWARD, false).animateXAxis();
                    new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.BACKWARD, true).animateXAxis();

                    this.animateSprite();

                    collisionCount = 0;
                    getSprite().enemyState = 0;
                    getSprite().signal = false;
                }
                else
                {
                    if (hasMove())
                    {
                        //forward move
                        UITransform attributes = new CombatUtils().getOpponent(w, true);
                        UITransform newAttr = new UITransform((attributes.xPos), attributes.yPos, attributes.zPos, attributes.width, attributes.height);

                        if (new CombatUtils().hasCollided(newAttr, new CombatUtils().getOpponent(w, false)))
                        {
                            //System.out.println("collided so moving backward");
                            lock.lock();
                            collisionCount++;
                            new CombatUtils().damagePlayer(w, (damage+10));
                            dialog.path = CombatUtils.pmText;
                            dialog.active = true;
                            dialog.activeUntil = System.currentTimeMillis() + 900;

                            new CombatAnimator(w, CombatPlayer.class, CombatPlayerWeapon.class, 65, CombatUtils.BACKWARD, false).animateXAxis();
                            if(new CombatUtils().getAbsHealth(w, false) <= 0)
                            {
                                scheduledExecutorService.shutdown();
                                w.deactivateCombat();
                                animateDeath(CombatPlayer.class, CombatPlayerWeapon.class, false);

                                dialog.path = CombatUtils.loserText;
                                dialog.active = true;
                                dialog.activeUntil = System.currentTimeMillis() + 9000;
                            }


                            new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.FORWARD, true).animateXAxis();
                            this.animateSprite();
                        }
                        else
                        {
                            //System.out.println("moving forward");
                            new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.FORWARD, true).animateXAxis();
                        }

                    }
                    else
                    {
                        //backwards move
                        new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.BACKWARD, true).animateXAxis();
                    }
                }

            }
            else
            {
                scheduledExecutorService.shutdown();
                w.deactivateCombat();
                state.playerSprite = 1;
                animateDeath(CombatEnemy.class, null, true);

                dialog.path = CombatUtils.winnerText;
                dialog.active = true;
                dialog.activeUntil = System.currentTimeMillis() + 9000;

            }


        }
        else
        {
            scheduledExecutorService.shutdown();
        }
    }


    public void animateDeath(Class<? extends Component> sprite, Class<? extends Component> weapon, boolean enemy)
    {
        this.au.playSound(ResourceLoader.resolve("sounds/enemy_death.wav"), false);
        new CombatAnimator(w, sprite, weapon, 150, CombatUtils.DOWN, enemy).animateYAxis();
    }




    public boolean hasMove()
    {
        int number = new Random().nextInt((10 - 1) + 1) + 1;
        ArrayList<Integer> chances = new ArrayList<>();
        if(damage == CombatUtils.ENEMY_DAMAGE)
        {
            chances = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));

        }
        else if(damage == CombatUtils.BOSS_DAMAGE){
            chances = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
        }
        else
        {
             chances = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        }

        if(chances.contains(number))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public int getCollisionMax()
    {
        if(damage == CombatUtils.ENEMY_DAMAGE) {
            return 3;
        }else if(damage == CombatUtils.BOSS_DAMAGE){
            return 2;
        }else{
            return 1;
        }
    }






}
