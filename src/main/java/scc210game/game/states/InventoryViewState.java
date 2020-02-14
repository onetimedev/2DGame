package scc210game.game.states;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
import scc210game.engine.ecs.World;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.state.event.KeyDepressedEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransNop;
import scc210game.engine.state.trans.TransPop;
import scc210game.engine.state.trans.Transition;
import scc210game.engine.ui.components.UIDraggable;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.Tuple2;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;
import scc210game.game.components.TextureStorage;
import scc210game.game.spawners.ui.InventorySlotSpawner;
import scc210game.game.states.events.LeaveInventoryEvent;

import java.util.Set;

public class InventoryViewState extends BaseInGameState {
    private static final int SLOTS_PER_ROW = 7;
    private static final float SLOT_SIZE = 0.13f;
    private static final float SLOT_H_OFFSET = 0.0275f;
    private static final float SLOT_SPACING = 0.005f;

    private final Query itemQuery = Query.builder()
            .require(Item.class)
            .build();

    private final World sourceWorld;
    private final Entity sourceInventoryEnt;
    private final Inventory sourceInventory;
    private Inventory inventory;

    public InventoryViewState(World sourceWorld, Entity sourceInventoryEnt, Inventory sourceInventory) {
        this.sourceWorld = sourceWorld;
        this.sourceInventoryEnt = sourceInventoryEnt;
        this.sourceInventory = sourceInventory;
    }

    private Entity findItem(int itemID, World world) {
        return world.applyQuery(this.itemQuery)
                .filter(e -> world.hasComponent(e, Item.class))
                .filter(e -> world.fetchComponent(e, Item.class).itemID == itemID)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public void onStart(World world) {
        this.cloneContentInto(world);

        for (int i = 0; i < this.inventory.slotCount; i++) {
            var x = (i % SLOTS_PER_ROW) * (SLOT_SIZE + SLOT_SPACING) + SLOT_H_OFFSET;
            var y = (i / SLOTS_PER_ROW) * (SLOT_SIZE + SLOT_SPACING);
            var slot = world.entityBuilder()
                    .with(new InventorySlotSpawner(x, y, SLOT_SIZE, SLOT_SIZE, this.inventory, i))
                    .build();

            var slotTransform = world.fetchComponent(slot, UITransform.class);
            var maybeItemID = this.inventory.getItemID(i);

            if (maybeItemID.isPresent()) {
                var itemID = maybeItemID.get();
                var itemEnt = this.findItem(itemID, world);

                var itemSize = new Vector2f(slotTransform.width * 0.8f, slotTransform.height * 0.8f);
                var centerPosition = UiUtils.centerTransforms(itemSize, slotTransform.pos(), slotTransform.size());

                world.addComponentToEntity(itemEnt, new UITransform(centerPosition.x, centerPosition.y, 4, itemSize.x, itemSize.y));
                world.addComponentToEntity(itemEnt, new Renderable(Set.of(ViewType.UI), 3, (Entity e, RenderWindow rw, World w) -> {
                    var trans = w.fetchComponent(e, UITransform.class);
                    var itemTex = w.fetchComponent(e, TextureStorage.class);
                    var sprite = new Sprite(itemTex.texture);
                    sprite.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));


                    var realItemSize = new Vector2f(itemTex.texture.getSize());
                    var mainViewSize = rw.getDefaultView().getSize().x;
                    var itemScale = trans.width / (realItemSize.x / mainViewSize);
                    sprite.setScale(itemScale, itemScale);
                    rw.draw(sprite);
                }));
                world.addComponentToEntity(itemEnt, new UIDraggable());
            }
        }
    }

    @Override
    public Transition handleEvent(StateEvent evt, World world) {
        if (evt instanceof KeyDepressedEvent) {
            KeyDepressedEvent evt1 = (KeyDepressedEvent) evt;
            if (evt1.key == Keyboard.Key.ESCAPE) {
                world.ecs.acceptEvent(new LeaveInventoryEvent(new Tuple2<>(this.sourceInventoryEnt, this.inventory)));
                return TransNop.getInstance();
            }
        }

        if (evt instanceof LeaveInventoryEvent) {
            return TransPop.getInstance();
        }

        return super.handleEvent(evt, world);
    }

    /**
     * Copy the inventory data from one world to another
     *
     * @param destWorld the world to copy the inventory into
     */
    private void cloneContentInto(World destWorld) {
        this.inventory = new Inventory(this.sourceInventory.slotCount);
        destWorld.entityBuilder()
                .with(this.inventory)
                .build();

        // clone all inventory
        this.sourceInventory.items().forEach(v -> {
            var srcItemEnt = this.findItem(v.l, this.sourceWorld);
            var srcItemComponents = this.sourceWorld.componentsOfEntity(srcItemEnt)
                    .map(c -> {
                        try {
                            return c.clone();
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    }).toArray(Component[]::new);

            destWorld.entityBuilder()
                    .with(srcItemComponents)
                    .build();

            this.inventory.addItemToSlot(v.l, v.r);
        });
    }
}
