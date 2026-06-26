package com.lobsterchops.stonebound.game.engine.gfx;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RenderPipeline {
 
    /**
     * A named render callback.  Lambdas and method references work because
     * this is a {@code @FunctionalInterface}.
     *
     * <p>The receiver gets access to the active {@link Renderer} rather than
     * a raw {@code Graphics2D} — all drawing must go through the renderer API.
     */
    @FunctionalInterface
    public interface RenderLayer {
        void render(Renderer renderer);
    }
 
    // ── State ────────────────────────────────────────────────────────────────
 
    private final Renderer renderer;
 
    /** Named system callbacks, ordered by RenderLayerKey. */
    private final EnumMap<RenderLayerKey, List<RenderLayer>> systemLayers =
            new EnumMap<>(RenderLayerKey.class);
 
    /** Per-frame Renderable submissions, ordered by RenderLayerKey. */
    private final EnumMap<RenderLayerKey, List<Renderable>> renderables =
            new EnumMap<>(RenderLayerKey.class);
 
    // ── Construction ─────────────────────────────────────────────────────────
 
    public RenderPipeline(Renderer renderer) {
        this.renderer = renderer;
        for (RenderLayerKey key : RenderLayerKey.values()) {
            systemLayers.put(key, new ArrayList<>());
            renderables.put(key, new ArrayList<>());
        }
    }
 
    // ── Registration API (persistent — survives frames) ───────────────────────
 
    /**
     * Adds a named system callback to a specific layer.
     * The callback persists across frames until explicitly removed.
     *
     * <p>Example:
     * <pre>
     *   pipeline.addLayer(RenderLayerKey.UI, screenManager::render);
     *   pipeline.addLayer(RenderLayerKey.GROUND, tileRenderer::render);
     * </pre>
     *
     * @param key   the layer to add this callback to
     * @param layer the render callback; must not be null
     */
    public void addLayer(RenderLayerKey key, RenderLayer layer) {
        systemLayers.get(key).add(layer);
    }
 
    /**
     * Removes a previously added system callback.
     *
     * @param key   the layer the callback was added to
     * @param layer the callback to remove
     */
    public void removeLayer(RenderLayerKey key, RenderLayer layer) {
        systemLayers.get(key).remove(layer);
    }
 
    // ── Per-frame submission API ──────────────────────────────────────────────
 
    /**
     * Submits a {@link Renderable} for the current frame only.
     * The submission is cleared automatically at the end of {@link #render}.
     *
     * <p>Call this during your update pass so objects can register themselves
     * each tick:
     * <pre>
     *   for (Entity e : activeEntities) {
     *       renderPipeline.submit(e);
     *   }
     * </pre>
     *
     * @param renderable the object to draw this frame; must not be null
     */
    public void submit(Renderable renderable) {
        renderables.get(renderable.getRenderLayer()).add(renderable);
    }
 
    // ── Frame render ──────────────────────────────────────────────────────────
 
    /**
     * Executes one complete frame.  Called by
     * {@link com.lobsterchops.stonebound.game.core.GamePanel#paintComponent}.
     *
     * <ol>
     *   <li>Opens the back buffer ({@link Renderer#begin()}).
     *   <li>For each layer in declaration order:
     *       <ol type="a">
     *         <li>Runs all registered system callbacks.
     *         <li>Calls {@link Renderable#render(Renderer)} on each submitted object.
     *       </ol>
     *   <li>Blits the back buffer to Swing and closes the context
     *       ({@link Renderer#end(Graphics)}).
     *   <li>Clears per-frame {@link Renderable} submissions for the next tick.
     * </ol>
     *
     * @param swingGraphics the Swing surface provided by {@code paintComponent};
     *                      this method never disposes it
     */
    public void render(Graphics swingGraphics) {
        renderer.begin();
 
        for (RenderLayerKey key : RenderLayerKey.values()) {
            // System callbacks (tile renderer, HUD, screens, etc.)
            for (RenderLayer layer : systemLayers.get(key)) {
                layer.render(renderer);
            }
            // Per-frame Renderable submissions (entities, effects, etc.)
            for (Renderable r : renderables.get(key)) {
                r.render(renderer);
            }
        }
 
        renderer.end(swingGraphics);
 
        // Clear per-frame submissions — system layers persist.
        for (RenderLayerKey key : RenderLayerKey.values()) {
            renderables.get(key).clear();
        }
    }
 
    /** Returns the underlying renderer (e.g. for GameContext setup). */
    public Renderer getRenderer() { return renderer; }
}