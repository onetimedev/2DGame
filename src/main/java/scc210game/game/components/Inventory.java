package scc210game.game.components;

import scc210game.engine.ecs.Component;
import scc210game.engine.ecs.Entity;
import scc210game.engine.utils.Tuple2;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Inventory extends Component {
    public final int slotCount;

    /**
     * map of item id to item entity
     */
    private final Map<Integer, Entity> itemIDEntities;

    /**
     * map of item entity to slot id
     */
    private final Map<Entity, Integer> itemEntSlotID;

    /**
     * map of slot id to slot
     */
    private final Map<Integer, Slot> slots;

    /**
     * set of slots not currently occupied
     */
    private final SortedSet<Integer> freeSlots;

    public Inventory(int slotCount) {
        this.slotCount = slotCount;
        this.freeSlots = IntStream.range(0, slotCount)
                .boxed()
                .collect(Collectors.toCollection(TreeSet::new));

        this.itemIDEntities = new HashMap<>();
        this.itemEntSlotID = new HashMap<>();

        this.slots = IntStream.range(0, slotCount)
                .boxed()
                .collect(Collectors.toMap(Function.identity(),
                        (id) -> new Slot(id, null)));
    }

    /**
     * Attempt to add an item to the inventory in any free slot
     * @param e the entity of the item to add
     * @param i the Item to add
     * @return true if the item was inserted, false if the inventory was full
     */
    public boolean addItem(Entity e, Item i) {
        if (this.freeSlots.isEmpty())
            return false;

        var slotID = this.freeSlots.first();
        this.freeSlots.remove(slotID);

        this.slots.get(slotID).itemID = i.itemID;
        this.itemIDEntities.put(i.itemID, e);
        this.itemEntSlotID.put(e, slotID);

        return true;
    }

    /**
     * Get the id of the slot an item is in, null if the item isn't in a slot of this inventory
     * @param e the item to fetch the slot of
     * @return null if the item isn't in a slot of this inventory, the slot id if it is
     */
    @Nullable public Integer getItemSlot(Entity e) {
        return this.itemEntSlotID.get(e);
    }

    /**
     * Add an item to a specific slot
     * @param e the Entity of the Item to add to the slot
     * @param i the Item to add to the slot
     * @param to the slot to add the item to
     * @return true if the item was inserted, false if the slot already had an item
     */
    public boolean addItemToSlot(Entity e, Item i, int to) {
        var slot = this.slots.get(to);
        if (slot.itemID != null)
            return false;

        this.freeSlots.remove(slot.slotID);
        this.itemIDEntities.put(i.itemID, e);
        slot.itemID = i.itemID;
        this.itemEntSlotID.put(e, slot.slotID);

        return true;
    }

    /**
     * Remove an item from this inventory, if it is contained in this inventory
     * @param e the entity of the item to remove
     * @param i the item to remove
     * @return true if the item was removed, false if the item wasn't in this inventory
     */
    public boolean removeItem(Entity e, Item i) {
        if (!this.itemIDEntities.containsKey(i.itemID))
            return false;

        this.itemIDEntities.remove(i.itemID);
        var slotID = this.itemEntSlotID.get(e);
        this.slots.get(slotID).itemID = null;
        this.freeSlots.add(slotID);
        this.itemEntSlotID.remove(e);

        return true;
    }

    /**
     * Test if a slot contains an item
     * @param slotID the slot id to test
     * @return true if the slot is full, false otherwise
     */
    public boolean slotFull(Integer slotID) {
        return this.slots.get(slotID).itemID != null;
    }

    /**
     * Get the item entity from a slot
     * @param slotID the slotID to get the entity from
     * @return the Entity representing the item in the slot
     */
    public Entity getSlotEntity(Integer slotID) {
        var slot = this.slots.get(slotID);
        return this.itemIDEntities.get(slot.itemID);
    }

    /**
     * Get items in this inventory, as a stream of tuples of (SlotID, Item)
     * @return a stream of tuples of (SlotID, Item)
     */
    public Stream<Tuple2<Integer, Entity>> items() {
        return this.slots.values()
                .stream()
                .filter(i -> i.itemID != null)
                .map(i -> new Tuple2<>(i.slotID, this.itemIDEntities.get(i.itemID)));
    }

    @Override
    public String serialize() {
        return null;
    }

    public static class Slot {
        private final int slotID;
        @Nullable
        public Integer itemID;

        public Slot(int slotID, @Nullable Integer itemID) {
            this.slotID = slotID;
            this.itemID = itemID;
        }
    }
}










