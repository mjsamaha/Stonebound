package com.lobsterchops.stonebound.game.ui.hud;

import java.awt.Color;
import java.awt.Graphics2D;

import com.lobsterchops.stonebound.game.config.ScreenConfig;
import com.lobsterchops.stonebound.game.engine.gfx.Renderer;
import com.lobsterchops.stonebound.game.gameplay.inventory.InventorySlot;
import com.lobsterchops.stonebound.game.ui.core.UILayer;



public class Hotbar implements UILayer {
 
    private static final int SLOT_COUNT = 9;
    private static final int SLOT_SIZE  = 40;
    private static final int Y_OFFSET   = ScreenConfig.HEIGHT - SLOT_SIZE - 10;
 
    private final InventorySlot[] slots = new InventorySlot[SLOT_COUNT];
    private int selectedIndex = 0;
 
    public Hotbar() {
        for (int i = 0; i < SLOT_COUNT; i++) slots[i] = new InventorySlot();
    }
 
    public void setSelected(int index) {
        this.selectedIndex = Math.max(0, Math.min(SLOT_COUNT - 1, index));
    }
 
    @Override
    public void update() { /* driven by input each tick */ }
 
 
    /**
     * Draws the hotbar using the Renderer API (screen-space, no camera offset).
     * Wire this as: {@code pipeline.addLayer(RenderLayerKey.UI, hotbar::render)}.
     */
    public void render(Renderer renderer) {
        int totalW = SLOT_COUNT * SLOT_SIZE;
        int startX = (ScreenConfig.WIDTH - totalW) / 2;
 
        for (int i = 0; i < SLOT_COUNT; i++) {
            int x = startX + i * SLOT_SIZE;
 
            // Slot background
            renderer.fillRect(i == selectedIndex ? Color.YELLOW : Color.DARK_GRAY,
                              x, Y_OFFSET, SLOT_SIZE, SLOT_SIZE);
            // Slot border
            renderer.drawRect(Color.WHITE, x, Y_OFFSET, SLOT_SIZE, SLOT_SIZE);
 
            // TODO: renderer.drawSpriteScreen(slots[i].getItem().getSprite(), x + 2, Y_OFFSET + 2)
        }
    }
 
 
    @Override
    public void render(Graphics2D g2) {
        int totalW = SLOT_COUNT * SLOT_SIZE;
        int startX = (ScreenConfig.WIDTH - totalW) / 2;
 
        for (int i = 0; i < SLOT_COUNT; i++) {
            int x = startX + i * SLOT_SIZE;
            g2.setColor(i == selectedIndex ? Color.YELLOW : Color.DARK_GRAY);
            g2.fillRect(x, Y_OFFSET, SLOT_SIZE, SLOT_SIZE);
            g2.setColor(Color.WHITE);
            g2.drawRect(x, Y_OFFSET, SLOT_SIZE, SLOT_SIZE);
        }
    }
}