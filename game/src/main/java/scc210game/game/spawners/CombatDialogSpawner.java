package scc210game.game.spawners;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.*;
import scc210game.engine.render.MainWindowResource;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.ResourceLoader;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.CombatDialog;

import java.io.IOException;
import java.lang.System;
import java.util.Set;

public class CombatDialogSpawner implements Spawner {

    private float xPosition = 0f;
    private float yPosition;
    private float width = 0.4f;
    private float height = 0.4f;

    private Texture t = new Texture();

    public CombatDialogSpawner(World world)
    {
        var rw = world.fetchGlobalResource(MainWindowResource.class);
        float xP = (float) rw.mainWindow.getSize().x/2;
        float yP = (float) rw.mainWindow.getSize().y/2;

        float nxP = xP/1920;

        xPosition = nxP;
        yPosition = 0.1f;

    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {




        var position = UiUtils.correctAspectRatio(new Vector2f(this.xPosition, this.yPosition));
        var size = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with(new UITransform(position.x, position.y, 0, size.x, size.y))
                .with(new Renderable(Set.of(ViewType.UI), 0,
                        (Entity e, RenderWindow rw, World w) -> {


                            var cDialog = w.applyQuery(Query.builder().require(CombatDialog.class).build()).findFirst().get();
                            var dialog = w.fetchComponent(cDialog, CombatDialog.class);

                            if(dialog.active && System.currentTimeMillis() < dialog.activeUntil)
                            {


                                loadTexture(dialog.path);
                                var dimensions = w.fetchComponent(e, UITransform.class);
                                Sprite bg = new Sprite(t);
                                bg.setScale(new Vector2f(1, 1));
                                bg.setPosition(UiUtils.convertUiPosition(rw, dimensions.pos()));
                                rw.draw(bg);
                            }
                            else
                            {
                                dialog.active = false;
                            }

                        }));

    }


    private void loadTexture(String path)
    {
        try
        {
            t.loadFromStream(ResourceLoader.resolve(path));
        }
        catch (IOException error)
        {
            error.printStackTrace();
        }
    }
}
