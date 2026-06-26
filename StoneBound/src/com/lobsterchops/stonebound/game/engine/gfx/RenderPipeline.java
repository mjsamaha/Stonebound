package com.lobsterchops.stonebound.game.engine.gfx;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


public class RenderPipeline {

    @FunctionalInterface
    public interface RenderLayer {
        void render(Graphics2D g2);
    }

    private final List<RenderLayer> layers = new ArrayList<>();
    private final Renderer renderer;

    public RenderPipeline(Renderer renderer) {
        this.renderer = renderer;
    }

    public void addLayer(RenderLayer layer) {
        layers.add(layer);
    }

    public void removeLayer(RenderLayer layer) {
        layers.remove(layer);
    }

    /** Called by {@link com.lobsterchops.stonebound.game.core.GamePanel#paintComponent}. */
    public void render(Graphics2D g2) {
        renderer.begin(g2);
        for (RenderLayer layer : layers) {
            layer.render(g2);
        }
    }

    public Renderer getRenderer() { return renderer; }
}
