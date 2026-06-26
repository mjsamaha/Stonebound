package com.lobsterchops.stonebound.game.gameplay.inventory;

import com.lobsterchops.stonebound.game.gameplay.item.Item;

public class Inventory {

    private final InventorySlot[] slots;
    private final int capacity;

    public Inventory(int capacity) {
        this.capacity = capacity;
        this.slots    = new InventorySlot[capacity];
        for (int i = 0; i < capacity; i++) slots[i] = new InventorySlot();
    }

    /** Returns true if item was added. */
    public boolean addItem(Item item, int qty) {
        for (InventorySlot slot : slots) {
            if (slot.isEmpty()) {
                slot.set(item, qty);
                return true;
            }
        }
        return false;
    }

    public InventorySlot getSlot(int index) { return slots[index]; }
    public int           getCapacity()      { return capacity; }
}
