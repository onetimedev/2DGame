package scc210game.game.states;

import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.state.InputHandlingState;
import scc210game.engine.state.event.KeyDepressedEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransPush;
import scc210game.engine.state.trans.TransReplaceAll;
import scc210game.engine.state.trans.Transition;
import scc210game.game.map.Map;
import scc210game.game.spawners.EnemySpawner;
import scc210game.game.spawners.MapSpawner;
import scc210game.game.spawners.PlayerSpawner;
import scc210game.game.states.events.ReturnToMainMenuEvent;
import scc210game.game.states.events.TogglePauseEvent;

/**
 * A base state for every state in our game, handles input and pausing etc
 */
public class BaseInGameState extends InputHandlingState {

    @Override
    public void onStart(World world) {
        world.entityBuilder().with(new MapSpawner()).build();
        world.entityBuilder().with(new PlayerSpawner()).build();

        var mapEnt = world.applyQuery(Query.builder().require(Map.class).build()).findFirst().get();
        var map = world.fetchComponent(mapEnt, Map.class);

        for(Vector2i v : map.getEnemyTiles()) {
            world.entityBuilder().with(new EnemySpawner()).build();
            // Sprite enemy = new Sprite(map.getTile(x, y).getTexture());
        }
    }


    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        // pause on escape key press
        if (evt instanceof KeyDepressedEvent) {
            KeyDepressedEvent evt1 = (KeyDepressedEvent) evt;
            if (evt1.key == Keyboard.Key.ESCAPE) {
                world.ecs.acceptEvent(new TogglePauseEvent());
            }
        }

        if (evt instanceof TogglePauseEvent) {
            return new TransPush(new PausedState());
        }

        if (evt instanceof ReturnToMainMenuEvent) {
            return new TransReplaceAll(new MainMenuState());
        }

        return super.handleEvent(evt, world);
    }
}
