package com.lobsterchops.stonebound.game.engine.gfx;


public interface Renderable {
 
    /**
     * The layer this object belongs to.  The pipeline uses this to sort draw
     * calls into the correct pass without the caller needing to know about the
     * pipeline's internal structure.
     *
     * @return the layer key — must not be null
     */
    RenderLayerKey getRenderLayer();
 
    /**
     * Performs all drawing for this object through the supplied renderer.
     *
     * <p>Do <em>not</em> call {@code renderer.getRaw().dispose()} — the renderer
     * owns the Graphics2D lifecycle.  Do <em>not</em> store the renderer
     * reference beyond this call.
     *
     * @param renderer the frame's active renderer; never null
     */
    void render(Renderer renderer);
}