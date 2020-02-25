package scc210game.game.spawners.ui;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Spawner;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.components.UIDroppable;
import scc210game.engine.ui.components.UIHovered;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;
import scc210game.game.components.ItemSlot;
import scc210game.game.events.ItemMoveEvent;

import java.awt.*;
import java.util.Set;

public class InventorySlotSpawner implements Spawner {
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final Inventory inventory;
    private final int slotID;

    public InventorySlotSpawner(float x, float y, float width, float height, Inventory inventory, int slotID) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.inventory = inventory;
        this.slotID = slotID;
    }

    @Override
    public World.EntityBuilder inject(World.EntityBuilder builder, World world) {
        var correctedPos = UiUtils.correctAspectRatio(new Vector2f(this.x, this.y));
        var correctedSize = UiUtils.correctAspectRatio(new Vector2f(this.width, this.height));

        return builder
                .with(new UITransform(correctedPos.x, correctedPos.y, 0, correctedSize.x, correctedSize.y))
                .with(new ItemSlot(this.inventory, this.slotID))
                .with(new UIDroppable((Entity thisEntity, Entity droppedEntity, World w) -> {
                    // only function on items
                    if (!w.hasComponent(droppedEntity, Item.class))
                        return;

                    var slot = w.fetchComponent(thisEntity, ItemSlot.class);

                    // if this slot has an item, don't do anything
                    if (slot.inventory.slotFull(slot.slotID))
                        return;

                    var item = w.fetchComponent(droppedEntity, Item.class);

                    w.eventQueue.broadcast(new ItemMoveEvent(item.itemID, slot.inventory, slot.slotID));
                }))
                .with(new Renderable(Set.of(ViewType.UI), 2, (Entity e, RenderWindow rw, World w) -> {
                    var trans = w.fetchComponent(e, UITransform.class);

                    var fillColour = w.hasComponent(e, UIHovered.class) ? Color.RED : Color.LIGHT_GRAY;

                    var rect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
                        this.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
                        this.setFillColor(UiUtils.transformColor(fillColour));
                        this.setOutlineColor(UiUtils.transformColor(Color.BLACK));
                    }};

                    rw.draw(rect);
                }));
    }
}
