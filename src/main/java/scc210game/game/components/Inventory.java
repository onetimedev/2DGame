package scc210game.game.components;

import scc210game.engine.ecs.Component;
import scc210game.engine.utils.Tuple2;
import scc210game.game.utils.BiMap;
import scc210game.game.utils.NamedTypeParam;

import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Inventory extends Component {
    public final int slotCount;

    /**
     * bimap of item id to slot id
     */
    private final BiMap<Integer, Integer> itemsslots;

    /**
     * set of slots not currently occupied
     */
    private final SortedSet<Integer> freeSlots;

    public Inventory(int slotCount) {
        this.slotCount = slotCount;
        this.freeSlots = IntStream.range(0, slotCount)
                .boxed()
                .collect(Collectors.toCollection(TreeSet::new));
        this.itemsslots = new BiMap<>();
    }

    /**
     * Attempt to add an item to the inventory in any free slot
     *
     * @param itemID the id of the item to add to the inventory
     * @return true if the item was inserted, false if the inventory was full
     */
    public boolean addItem(int itemID) {
        if (this.freeSlots.isEmpty())
            return false;

        var slotID = this.freeSlots.first();
        this.freeSlots.remove(slotID);

        this.itemsslots.put(itemID, slotID);

        return true;
    }

    /**
     * Get the id of the slot an item is in, None if the item isn't in a slot of this inventory
     *
     * @param itemID the item id to fetch the slot of
     * @return None if the item isn't in a slot of this inventory, the slot id if it is
     */
    public Optional<Integer> getSlotID(int itemID) {
        return Optional.ofNullable(this.itemsslots.getByLeft(itemID));
    }

    /**
     * Get the id of the item a that is in a given slot, None if the item isn't in a slot of this inventory
     *
     * @param slotID the slot for which to get the itemID of
     * @return None if the item isn't in a slot of this inventory, the slot id if it is
     */
    public Optional<Integer> getItemID(int slotID) {
        return Optional.ofNullable(this.itemsslots.getByRight(slotID));
    }

    /**
     * Add an item to a specific slot
     *
     * @param itemID the id of the item to add to the slot
     * @param slotID the id of the slot to add the item to
     * @apiNote this assumes the slot exists in the inventory and is free
     */
    public void addItemToSlot(int itemID, int slotID) {
        this.itemsslots.put(itemID, slotID);
    }

    /**
     * Remove an item from this inventory, if it is contained in this inventory
     *
     * @param itemID the id of the item to remove
     * @return true if the item was removed, false if the item wasn't in this inventory
     */
    public boolean removeItem(int itemID) {
        if (!this.itemsslots.containsLeft(itemID))
            return false;

        var slotID = this.itemsslots.getByLeft(itemID);
        this.itemsslots.removeByLeft(itemID);
        this.freeSlots.add(slotID);

        return true;
    }

    /**
     * Test if a slot contains an item
     *
     * @param slotID the slot id to test
     * @return true if the slot is full, false otherwise
     */
    public boolean slotFull(int slotID) {
        return !this.freeSlots.contains(slotID);
    }

    /**
     * Get slots and items in this inventory, as a stream of tuples of (SlotID, ItemID)
     *
     * @return a stream of tuples of (SlotID, ItemID)
     */
    public Stream<Tuple2<@NamedTypeParam(name = "slotID") Integer, @NamedTypeParam(name = "itemID") Integer>> items() {
        return this.itemsslots.items();
    }

    @Override
    public String serialize() {
        return null;
    }
}










