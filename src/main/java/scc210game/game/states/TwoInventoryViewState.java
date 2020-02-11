package scc210game.game.states;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.ecs.Query;
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
import scc210game.engine.utils.Tuple2;
import scc210game.engine.utils.UiUtils;
import scc210game.game.components.Inventory;
import scc210game.game.components.Item;
import scc210game.game.components.TextureStorage;
import scc210game.game.spawners.ui.InventorySlotSpawner;
import scc210game.game.states.events.LeaveInventoryEvent;

import java.util.Set;

public class TwoInventoryViewState extends BaseInGameState {
    private static final int SLOTS_PER_ROW = 7;
    private static final float SLOT_SIZE = 0.05f;
    private static final float SLOT_SPACING = 0.005f;

    private final Query itemQuery = Query.builder()
            .require(Item.class)
            .build();

    private final World sourceWorld;
    private final Entity sourceInventoryEnt0;
    private final Inventory sourceInventory0;
    private final Entity sourceInventoryEnt1;
    private final Inventory sourceInventory1;
    private Inventory inventory0;
    private Inventory inventory1;

    public TwoInventoryViewState(World sourceWorld, Entity sourceInventoryEnt0, Inventory sourceInventory0, Entity sourceInventoryEnt1, Inventory sourceInventory1) {
        this.sourceWorld = sourceWorld;
        this.sourceInventoryEnt0 = sourceInventoryEnt0;
        this.sourceInventory0 = sourceInventory0;
        this.sourceInventoryEnt1 = sourceInventoryEnt1;
        this.sourceInventory1 = sourceInventory1;
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
        this.inventory0 = this.cloneContentInto(world, this.sourceInventory0);
        this.inventory1 = this.cloneContentInto(world, this.sourceInventory1);

        spawnInventory(world, this.inventory0, 0.0f);
        spawnInventory(world, this.inventory1, 0.3f);
    }

    private void spawnInventory(World world, Inventory inventory, float vOffs) {
        for (int i = 0; i < inventory.slotCount; i++) {
            var x = (i % SLOTS_PER_ROW) * (SLOT_SIZE + SLOT_SPACING);
            var y = (i / SLOTS_PER_ROW) * (SLOT_SIZE + SLOT_SPACING) + vOffs;
            var slot = world.entityBuilder()
                    .with(new InventorySlotSpawner(x, y, SLOT_SIZE, SLOT_SIZE, inventory, i))
                    .build();

            var slotTransform = world.fetchComponent(slot, UITransform.class);
            var maybeItemID = inventory.getItemID(i);

            if (maybeItemID.isPresent()) {
                var itemID = maybeItemID.get();
                var itemEnt = this.findItem(itemID, world);
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
                world.ecs.acceptEvent(new LeaveInventoryEvent(
                        new Tuple2<>(this.sourceInventoryEnt0, this.inventory0),
                        new Tuple2<>(this.sourceInventoryEnt1, this.inventory1)));
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
     * @param sourceInventory the inventory to clone
     */
    private Inventory cloneContentInto(World destWorld, Inventory sourceInventory) {
        var inventory = new Inventory(sourceInventory.slotCount);
        destWorld.entityBuilder()
                .with(inventory)
                .build();

        // clone all inventory
        sourceInventory.items().forEach(v -> {
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

            inventory.addItemToSlot(v.l, v.r);
        });

        return inventory;
    }
}
