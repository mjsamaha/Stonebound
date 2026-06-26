package com.lobsterchops.stonebound.game.ui.core;

import java.awt.Color;
import java.awt.Font;

import com.lobsterchops.stonebound.game.engine.gfx.Renderer;
import com.lobsterchops.stonebound.game.engine.util.DebugMetrics;

public class DebugOverlay implements UILayer {
	 
    private static final Font  FONT  = new Font("Monospaced", Font.PLAIN, 12);
    private static final Color COLOR = Color.YELLOW;
 
    private final DebugMetrics metrics;
    private boolean enabled = true;
 
    /**
     * @param metrics live metrics source; must not be null
     */
    public DebugOverlay(DebugMetrics metrics) {
        this.metrics = metrics;
    }
 
 
    @Override
    public void update() {
        // Reads live from metrics each render pass — no local state needed.
    }
 
    /**
     * Draws the debug overlay through the renderer API.
     * Wire this as: {@code pipeline.addLayer(RenderLayerKey.UI, debugOverlay::render)}.
     *
     * @param renderer the active renderer; must not be null
     */
    public void render(Renderer renderer) {
        if (!enabled) return;
 
        int y = 30;
        renderer.drawText("FPS: " + metrics.getFps(), COLOR, FONT, 10, y);
        // Add more metrics here as you expose them from DebugMetrics:
        // renderer.drawText("Entities: " + metrics.getEntityCount(), COLOR, FONT, 10, y += 16);
        // renderer.drawText("Cam: " + camera.getOffsetX() + ", " + camera.getOffsetY(), COLOR, FONT, 10, y += 16);
    }
 
    /**
     * Legacy UILayer render — delegates to the Renderer overload via getRaw().
     * Prefer wiring {@link #render(Renderer)} as a pipeline layer callback.
     */
    @Override
    public void render(java.awt.Graphics2D g2) {
        // Kept to satisfy UILayer for any existing code that calls it directly.
        // When wired as a pipeline callback, render(Renderer) is called instead.
        if (!enabled) return;
        g2.setFont(FONT);
        g2.setColor(COLOR);
        g2.drawString("FPS: " + metrics.getFps(), 10, 30);
    }
 
 
    @Override
    public boolean isVisible() { return enabled; }
 
    public void toggle()                       { enabled = !enabled; }
    public void setEnabled(boolean enabled)    { this.enabled = enabled; }
}