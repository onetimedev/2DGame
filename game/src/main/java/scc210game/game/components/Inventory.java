package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.engine.utils.Tuple2;
import scc210game.game.utils.BiMap;
import scc210game.game.utils.LoadJsonNum;
import scc210game.game.utils.NamedTypeParam;

import java.util.*;
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

        this.freeSlots.remove(slotID);
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
     * Reset the state of the inventory to having no items, and every slot free
     */
    public void clear() {
        this.itemsslots.clear();
        for (var i = 0; i < slotCount; i++)
            this.freeSlots.add(i);
    }

    /**
     * Get slots and items in this inventory, as a stream of tuples of (SlotID, ItemID)
     *
     * @return a stream of tuples of (ItemID, SlotID)
     */
    public Stream<Tuple2<@NamedTypeParam(name = "itemID") Integer, @NamedTypeParam(name = "slotID") Integer>> items() {
        return this.itemsslots.items();
    }

    static {
        register(Inventory.class, j -> {
            var json = (JsonObject) j;
            var slotCount = LoadJsonNum.loadInt(json.get("slotCount"));
            var itemsSlotsS = (JsonArray) json.get("itemsSlots");
            var itemsSlots = itemsSlotsS.stream()
                    .map(slotS -> {
                        var slotA = (JsonArray) slotS;
                        return new Tuple2<>(LoadJsonNum.loadInt(slotA.get(0)), LoadJsonNum.loadInt(slotA.get(1)));
                    })
                    .collect(Collectors.toList());

            var inv = new Inventory(slotCount);
            itemsSlots.forEach(t ->
                    inv.addItemToSlot(t.l, t.r));
            return inv;
        });
    }

    @Override
    public Jsonable serialize() {
        return new JsonObject(Map.of(
                "slotCount", this.slotCount,
                "itemsSlots", this.itemsslots.items().map(t -> new JsonArray(List.of(t.l, t.r))).collect(Collectors.toList())
        ));
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "slotCount=" + slotCount +
                ", itemsslots=" + itemsslots +
                ", freeSlots=" + freeSlots +
                '}';
    }

    @Override
    public Inventory copy() {
        var inv = new Inventory(slotCount);
        this.items().forEach(t ->
                inv.addItemToSlot(t.l, t.r));
        return inv;
    }
}










