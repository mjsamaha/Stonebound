package com.lobsterchops.stonebound.game.ui.features.inventory;

import java.awt.Color;
import java.awt.Graphics2D;

import com.lobsterchops.stonebound.game.gameplay.inventory.InventorySlot;


public class ItemSlot {

    private final int x, y;
    private static final int SIZE = 36;
    private InventorySlot data;

    public ItemSlot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setData(InventorySlot data) { this.data = data; }

    public void render(Graphics2D g2) {
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(x, y, SIZE, SIZE);
        g2.setColor(Color.GRAY);
        g2.drawRect(x, y, SIZE, SIZE);
        // TODO: draw item icon
    }
}
