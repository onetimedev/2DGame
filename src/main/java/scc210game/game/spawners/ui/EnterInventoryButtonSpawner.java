package scc210game.game.spawners.ui;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UIClickable;
import scc210game.engine.ui.components.UIHovered;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
import scc210game.game.Shaders;
import scc210game.game.components.Inventory;
import scc210game.game.components.SelectedWeaponInventory;
import scc210game.game.components.TextureStorage;
import scc210game.game.map.Player;
import scc210game.game.states.events.EnterInventoryEvent;

import java.util.Set;

public class EnterInventoryButtonSpawner implements Spawner {
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public EnterInventoryButtonSpawner(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    private static void render(Entity e, RenderWindow rw, World w) {
        var trans = w.fetchComponent(e, UITransform.class);
        var tex = w.fetchComponent(e, TextureStorage.class);

        var sprite = new Sprite(tex.getTexture());

        sprite.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));

        if (w.hasComponent(e, UIHovered.class))
            rw.draw(sprite, new RenderStates(Shaders.lighter));
        else
            rw.draw(sprite);
    }

    private static void onClick(Entity e, World w) {
        var player = w.applyQuery(Query.builder().require(Player.class).build()).findFirst().orElseThrow();
        var inv = w.fetchComponent(player, Inventory.class);
        var selectedWeapon = w.applyQuery(Query.builder().require(SelectedWeaponInventory.class).build()).findFirst().orElseThrow();
        var sw = w.fetchComponent(selectedWeapon, Inventory.class);
        w.ecs.acceptEvent(new EnterInventoryEvent(inv, sw, player, selectedWeapon));
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        var correctedPos = UiUtils.correctAspectRatio(new Vector2f(this.x, this.y));
        var correctedSize = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with(new UITransform(correctedPos.x, correctedPos.y, 0, correctedSize.x, correctedSize.y))
                .with(new TextureStorage("textures/map/chest.png"))
                .with(new UIClickable(EnterInventoryButtonSpawner::onClick))
                .with(new Renderable(Set.of(ViewType.UI), 2, EnterInventoryButtonSpawner::render));
    }
}
