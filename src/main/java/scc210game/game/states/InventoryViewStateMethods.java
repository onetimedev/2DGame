package scc210game.game.states;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import scc210game.engine.animation.Animate;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.ui.Font;
import scc210game.engine.ui.components.UIDraggable;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.HasToolTip;
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;
import scc210game.game.components.TextureStorage;
import scc210game.game.spawners.ui.InventorySlotSpawner;

import java.awt.*;
import java.util.Set;

public class InventoryViewStateMethods extends BaseInGameState {
    private static final int SLOTS_PER_ROW = 7;
    private static final float SLOT_SIZE = 0.13f;
    private static final float SLOT_H_OFFSET = 0.0275f;
    private static final float SLOT_SPACING = 0.005f;
    protected final World sourceWorld;
    private final Query itemQuery = Query.builder()
            .require(Item.class)
            .build();

    public InventoryViewStateMethods(World sourceWorld) {
        this.sourceWorld = sourceWorld;
    }

    protected Entity findItem(int itemID, World world) {
        return world.applyQuery(this.itemQuery)
                .filter(e -> world.hasComponent(e, Item.class))
                .filter(e -> world.fetchComponent(e, Item.class).itemID == itemID)
                .findFirst()
                .orElseThrow();
    }

    protected void transformItemToInventory(World world, UITransform slotTransform, int itemID) {
        var itemEnt = this.findItem(itemID, world);
        var itemData = world.fetchComponent(itemEnt, Item.class);

        var itemSize = new Vector2f(slotTransform.width * 0.8f, slotTransform.height * 0.8f);
        var centerPosition = UiUtils.centerTransforms(itemSize, slotTransform.pos(), slotTransform.size());

        world.addComponentToEntity(itemEnt, new UITransform(centerPosition.x, centerPosition.y, 4, itemSize.x, itemSize.y));
        world.addComponentToEntity(itemEnt, new HasToolTip());
        world.addComponentToEntity(itemEnt, new Renderable(Set.of(ViewType.UI), 3, (Entity e, RenderWindow rw, World w) -> {
            var trans = w.fetchComponent(e, UITransform.class);
            var itemTex = w.fetchComponent(e, TextureStorage.class);
            var sprite = new Sprite(itemTex.getTexture());
            sprite.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));


            var realItemSize = new Vector2f(itemTex.getTexture().getSize());
            var mainViewSize = rw.getDefaultView().getSize().x;
            var itemScale = trans.width / (realItemSize.x / mainViewSize);
            sprite.setScale(itemScale, itemScale);
            rw.draw(sprite);

            var tooltipPos = new Vector2f(trans.xPos, trans.yPos + trans.height + 0.01f);

            var toolTip = w.fetchComponent(e, HasToolTip.class);

            if (!toolTip.isVisible())
                return;

            float opacity;
            if (w.hasComponent(e, Animate.class)) {
                opacity = toolTip.calcOpacity(w.fetchComponent(e, Animate.class).pctComplete);
            } else {
                opacity = 1.0f;
            }

            var text = new Text(itemData.tooltipString(), Font.fantasqueSansMono) {{
                var colour = new Color(1.0f, 1.0f, 1.0f, opacity);
                this.setPosition(UiUtils.convertUiPosition(rw, tooltipPos));
                this.setColor(UiUtils.transformColor(colour));
            }};

            var textBounds = text.getGlobalBounds();
            var rect = new RectangleShape(new Vector2f(textBounds.width, textBounds.height)) {{
                var colour = new Color(0.4f, 0.4f, 0.4f, opacity);
                this.setPosition(new Vector2f(textBounds.left, textBounds.top));
                this.setFillColor(UiUtils.transformColor(colour));
                this.setOutlineColor(UiUtils.transformColor(colour));
                this.setOutlineThickness(4f);
            }};

            rw.draw(rect);
            rw.draw(text);
        }));
        world.addComponentToEntity(itemEnt, new UIDraggable());
    }

    protected void spawnInventory(World world, Inventory inventory, float vOffs) {
        for (int i = 0; i < inventory.slotCount; i++) {
            var x = (i % SLOTS_PER_ROW) * (SLOT_SIZE + SLOT_SPACING) + SLOT_H_OFFSET;
            var y = (i / SLOTS_PER_ROW) * (SLOT_SIZE + SLOT_SPACING) + vOffs;
            var slot = world.entityBuilder()
                    .with(new InventorySlotSpawner(x, y, SLOT_SIZE, SLOT_SIZE, inventory, i))
                    .build();

            var slotTransform = world.fetchComponent(slot, UITransform.class);
            var maybeItemID = inventory.getItemID(i);

            maybeItemID.ifPresent(itemID -> transformItemToInventory(world, slotTransform, itemID));
        }
    }

    /**
     * Copy the inventory data from one world to another
     *
     * @param destWorld the world to copy the inventory into
     * @param sourceInventory the inventory to clone
     */
    protected Inventory cloneContentInto(World destWorld, Inventory sourceInventory) {
        var inventory = new Inventory(sourceInventory.slotCount);
        destWorld.entityBuilder()
                .with(inventory)
                .build();

        // clone all inventory
        sourceInventory.items().forEach(v -> {
            var srcItemEnt = this.findItem(v.l, this.sourceWorld);
            var srcItemComponents = this.sourceWorld.componentsOfEntity(srcItemEnt)
                    .map(Component::copy).toArray(Component[]::new);

            destWorld.entityBuilder()
                    .with(srcItemComponents)
                    .build();

            inventory.addItemToSlot(v.l, v.r);
        });

        return inventory;
    }
}
