package com.lobsterchops.stonebound.game.gameplay.inventory;

public class InventorySystem {

    public void transfer(Inventory from, int fromSlot, Inventory to, int toSlot) {
        InventorySlot src = from.getSlot(fromSlot);
        InventorySlot dst = to.getSlot(toSlot);
        if (src.isEmpty()) return;
        if (dst.isEmpty()) {
            dst.set(src.getItem(), src.getQuantity());
            src.clear();
        }
        // TODO: merge stacks if same item type
    }
}
