package scc210game.game.states;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.World;
import scc210game.engine.render.MainViewResource;
import scc210game.engine.render.Renderable;
import scc210game.engine.render.ViewType;
import scc210game.engine.state.event.KeyDepressedEvent;
import scc210game.engine.state.event.StateEvent;
import scc210game.engine.state.trans.TransNop;
import scc210game.engine.state.trans.TransPop;
import scc210game.engine.state.trans.Transition;
import scc210game.engine.ui.components.UIDraggable;
import scc210game.engine.ui.components.UITransform;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;
import scc210game.game.components.TextureStorage;
import scc210game.game.spawners.ui.InventorySlotSpawner;
import scc210game.game.states.events.LeaveInventoryEvent;

import java.util.Set;

public class InventoryViewState extends BaseInGameState {
    private static final int SLOTS_PER_ROW = 7;
    private static final float SLOT_SIZE = 0.05f;
    private static final float SLOT_SPACING = 0.005f;

    private final World sourceWorld;
    private final Inventory sourceInventory;
    private Inventory inventory;

    public InventoryViewState(World sourceWorld, Inventory sourceInventory) {
        this.sourceWorld = sourceWorld;
        this.sourceInventory = sourceInventory;
    }

    @Override
    public void onStart(World world) {
        this.cloneContentInto(world);

        for (int i = 0; i < this.inventory.slotCount; i++) {
            var x = (i % SLOTS_PER_ROW) * (SLOT_SIZE + SLOT_SPACING);
            var y = (i / SLOTS_PER_ROW) * (SLOT_SIZE + SLOT_SPACING);
            var slot = world.entityBuilder()
                    .with(new InventorySlotSpawner(x, y, SLOT_SIZE, SLOT_SIZE, this.inventory, i))
                    .build();

            var slotTransform = world.fetchComponent(slot, UITransform.class);

            if (this.inventory.slotFull(i)) {
                var itemEnt = this.inventory.getSlotEntity(i);
                var tex = world.fetchComponent(itemEnt, TextureStorage.class);

                var mainView = world.fetchGlobalResource(MainViewResource.class);

                var realItemSize = new Vector2f(tex.texture.getSize());
                var mainViewSize = mainView.mainView.getSize().x;
                var itemSize = new Vector2f(realItemSize.x / mainViewSize, realItemSize.y / mainViewSize);
                var position = UiUtils.centerTransforms(itemSize, slotTransform.pos(), slotTransform.size());

                world.addComponentToEntity(itemEnt, new UITransform(position.x, position.y, 4, itemSize.x, itemSize.y));
                world.addComponentToEntity(itemEnt, new Renderable(Set.of(ViewType.UI), 3, (Entity e, RenderWindow rw, World w) -> {
                    var trans = w.fetchComponent(e, UITransform.class);
                    var itemTex = w.fetchComponent(e, TextureStorage.class);
                    var sprite = new Sprite(itemTex.texture);
                    sprite.setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
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
                world.ecs.acceptEvent(new LeaveInventoryEvent(this.inventory));
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
        var inventoryEnt = destWorld.entityBuilder()
                .with(this.inventory)
                .build();

        // clone all inventory
        this.sourceInventory.items().forEach(v -> {
            var itemEnt = destWorld.entityBuilder()
                    .with(this.sourceWorld.componentsOfEntity(v.r).map(c -> {
                        try {
                            return c.clone();
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    }).toArray(Component[]::new))
                    .build();
            var item = destWorld.fetchComponent(itemEnt, Item.class);
            this.inventory.addItemToSlot(itemEnt, item, v.l);
        });
    }
}
