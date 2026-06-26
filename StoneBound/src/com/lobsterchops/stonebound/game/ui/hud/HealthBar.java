package com.lobsterchops.stonebound.game.ui.hud;

import java.awt.Color;
import java.awt.Graphics2D;

import com.lobsterchops.stonebound.game.engine.gfx.Renderer;
import com.lobsterchops.stonebound.game.ui.core.UILayer;



public class HealthBar implements UILayer {
 
    private int currentHp;
    private int maxHp;
 
    private static final int X = 10, Y = 10, W = 120, H = 12;
 
    public void setHealth(int current, int max) {
        this.currentHp = current;
        this.maxHp     = max;
    }
 
    @Override
    public void update() { /* driven by player state each tick */ }
 
    // ── Renderer overload (preferred) ────────────────────────────────────────
 
    /**
     * Draws the health bar using the Renderer API.
     * Wire this as: {@code pipeline.addLayer(RenderLayerKey.UI, healthBar::render)}.
     */
    public void render(Renderer renderer) {
        // Background
        renderer.fillRect(Color.DARK_GRAY, X, Y, W, H);
        // Health fill
        if (maxHp > 0) {
            float pct = (float) currentHp / maxHp;
            renderer.fillRect(Color.RED, X, Y, (int)(W * pct), H);
        }
        // Border
        renderer.drawRect(Color.WHITE, X, Y, W, H);
    }
 
    // ── Legacy UILayer render (Graphics2D) — kept for interface compatibility ──
 
    @Override
    public void render(Graphics2D g2) {
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(X, Y, W, H);
        if (maxHp > 0) {
            float pct = (float) currentHp / maxHp;
            g2.setColor(Color.RED);
            g2.fillRect(X, Y, (int)(W * pct), H);
        }
        g2.setColor(Color.WHITE);
        g2.drawRect(X, Y, W, H);
    }
}