package scc210game.engine.combat;

import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.ui.components.UITransform;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatEnemyWeapon;
import scc210game.game.components.CombatPlayer;
import scc210game.game.components.CombatPlayerWeapon;

import java.io.IOException;
import java.nio.file.Paths;
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


    public EnemyController(World w, Class<? extends Component> spriteClass, int damage){
        this.w = w;
        this.spriteClass = spriteClass;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.damage = damage;
        start();
    }

    private void start()
    {


        if(startFirst()){
            start = true;
            scheduledExecutorService.scheduleAtFixedRate(this::initMove, 0, 800, TimeUnit.MILLISECONDS);

        }
        else
        {
            if(start)
            {
                scheduledExecutorService.scheduleAtFixedRate(this::initMove, 0, 800, TimeUnit.MILLISECONDS);

            }

        }


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
        //scheduledExecutorService.schedule(this::animate, 200, TimeUnit.MILLISECONDS);
        getSprite().nextChange = System.currentTimeMillis() + 60;

        if(getSprite().state < maxFrame()) {
            getSprite().state++;
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
        if(w.getActiveAnimation())
        {
            if(getHealth() > 0) {

                if (collisionCount >= 3) {
                    new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.BACKWARD, true).animateXAxis();
                    collisionCount = 0;
                    getSprite().state = 0;
                    getSprite().signal = false;
                } else {
                    if (getMove() != 3) {
                        //forward move
                        //System.out.println("enemy moving forward");
                        UITransform attributes = new CombatUtils().getOpponent(w, true);
                        float collisionXPos = attributes.xPos + (CombatUtils.X_AXIS_MOVE_DISTANCE * 15);
                        UITransform newAttr = new UITransform(attributes.xPos, attributes.yPos, attributes.zPos, attributes.width, attributes.height);

                        if (new CombatUtils().hasCollided(newAttr, new CombatUtils().getOpponent(w, false))) {
                            //System.out.println("collided so moving backward");
                            collisionCount++;
                            new CombatUtils().damagePlayer(w, damage);

                            if(new CombatUtils().getAbsHealth(w, false) <= 0)
                            {
                                animateDeath(CombatPlayer.class, CombatPlayerWeapon.class, true);
                            }


                            new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.FORWARD, true).animateXAxis();
                            this.animateSprite();
                        } else {
                            //System.out.println("moving forward");
                            new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.FORWARD, true).animateXAxis();
                        }
                    } else if (getMove() != 4) {
                        //backwards move
                        //System.out.println("enemy moving backward");
                        new CombatAnimator(w, CombatEnemy.class, CombatEnemyWeapon.class, 15, CombatUtils.BACKWARD, true).animateXAxis();
                    } else {
                        //System.out.println("no move");
                    }
                }

            }else
            {
                animateDeath(CombatEnemy.class, null, true);
                scheduledExecutorService.shutdown();
            }


        }else{
            scheduledExecutorService.shutdown();
        }
    }


    public void animateDeath(Class<? extends Component> sprite, Class<? extends Component> weapon, boolean enemy)
    {
        new CombatAnimator(w, sprite, weapon, 150, CombatUtils.DOWN, enemy).animateYAxis();
    }



    private int getMove()
    {
        return new Random().nextInt((10 - 1) + 1) + 1;
    }


    private boolean startFirst()
    {
        int number = new Random().nextInt((100 - 1) + 1) + 1;
        if(number >= 80 && number <= 100){
            //20% chance
            return true;
        }

        return false;
    }



    public void setStart(boolean bool)
    {
        this.start = bool;
    }

    public void endFight()
    {
        fight = false;
    }

    public void startFight()
    {
        fight = true;
    }


}
