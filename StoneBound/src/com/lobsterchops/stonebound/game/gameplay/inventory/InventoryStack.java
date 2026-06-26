package com.lobsterchops.stonebound.game.gameplay.inventory;

import com.lobsterchops.stonebound.game.gameplay.item.Item;

public record InventoryStack(Item item, int quantity) {

    public static final InventoryStack EMPTY = new InventoryStack(null, 0);

    public boolean isEmpty() { return item == null || quantity <= 0; }

    public InventoryStack withQuantity(int newQty) {
        return new InventoryStack(item, newQty);
    }
}
