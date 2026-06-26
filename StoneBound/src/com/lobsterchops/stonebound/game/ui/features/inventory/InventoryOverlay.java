package com.lobsterchops.stonebound.game.ui.features.inventory;

import java.awt.Graphics2D;

import com.lobsterchops.stonebound.game.ui.core.Overlay;


public class InventoryOverlay extends Overlay {

    @Override public void update() {}

    @Override
    public void render(Graphics2D g2) {
        if (!visible) return;
        // TODO: draw background panel + item slots
    }
}
