package com.lobsterchops.stonebound.game.gameplay.inventory;

import com.lobsterchops.stonebound.game.gameplay.item.Item;

public class InventorySlot {

    private Item item;
    private int  quantity;

    public InventorySlot() {}

    public boolean isEmpty()         { return item == null || quantity == 0; }
    public Item    getItem()         { return item; }
    public int     getQuantity()     { return quantity; }

    public void set(Item item, int quantity) {
        this.item     = item;
        this.quantity = quantity;
    }

    public void clear() {
        item     = null;
        quantity = 0;
    }
}
