package scc210game.game.spawners.ui;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.CombatBackgroundComponent;
import scc210game.game.components.CombatEnemy;
import scc210game.game.components.CombatPlayer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class CombatBackground implements Spawner {

    private float xPosition = 0f;
    private float yPosition = 0f;
    private float width = 1f;
    private float height = 1f;

    private String bg;
    private Texture t = new Texture();

    public CombatBackground(String background)
    {
        this.bg = background;

        try {
            t.loadFromFile(Paths.get(bg));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        var position = UiUtils.correctAspectRatio(new Vector2f(this.xPosition, this.yPosition));
        var size = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with(new CombatBackgroundComponent(this.bg))
                .with(new UITransform(position.x, position.y, 0, size.x, size.y))
                .with(new Renderable(Set.of(ViewType.UI), 0,
                        (Entity e, RenderWindow rw, World w) -> {

                            var dimensions = w.fetchComponent(e, UITransform.class);
                            Sprite bg = new Sprite(t);
                            bg.setScale(new Vector2f(1,1));
                            bg.setPosition(UiUtils.convertUiPosition(rw, dimensions.pos()));
                            rw.draw(bg);


                }));

    }
}
