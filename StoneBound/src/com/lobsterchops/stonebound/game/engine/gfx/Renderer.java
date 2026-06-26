package com.lobsterchops.stonebound.game.engine.gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Renderer {

    private Graphics2D g2;
    private final Camera camera;

    public Renderer(Camera camera) {
        this.camera = camera;
    }

    /** Called at the start of every frame by {@link RenderPipeline}. */
    public void begin(Graphics2D g2) {
        this.g2 = g2;
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    // ── World-space drawing (camera-offset applied) ──────────────────────────

    public void drawSprite(Sprite sprite, float worldX, float worldY) {
        g2.drawImage(sprite.getImage(),
                camera.toScreenX(worldX),
                camera.toScreenY(worldY),
                null);
    }

    // ── Screen-space drawing (no camera offset) ───────────────────────────────

    public void drawSpriteScreen(Sprite sprite, int x, int y) {
        sprite.draw(g2, x, y);
    }

    public void fillRect(Color color, int x, int y, int w, int h) {
        g2.setColor(color);
        g2.fillRect(x, y, w, h);
    }

    public void drawRect(Color color, int x, int y, int w, int h) {
        g2.setColor(color);
        g2.drawRect(x, y, w, h);
    }

    public void drawString(String text, Color color, Font font, int x, int y) {
        g2.setColor(color);
        g2.setFont(font);
        g2.drawString(text, x, y);
    }

    public Graphics2D getRaw() { return g2; }
    public Camera     getCamera() { return camera; }
}