package com.lobsterchops.stonebound.game.engine.gfx;

import com.lobsterchops.stonebound.game.engine.math.Rectangle;
import com.lobsterchops.stonebound.game.gameplay.world.World;

public class TileRenderer {
	 
    private final World       world;
    private final Camera      camera;
    private final SpriteSheet spriteSheet;
    private final int         tileSize;
 
    /**
     * @param world       the world to read tile IDs from
     * @param camera      used to compute the visible tile range
     * @param spriteSheet tile sprite sheet, one frame per tile ID
     * @param tileSize    rendered tile size in pixels (usually 16)
     */
    public TileRenderer(World world, Camera camera,
                        SpriteSheet spriteSheet, int tileSize) {
        this.world       = world;
        this.camera      = camera;
        this.spriteSheet = spriteSheet;
        this.tileSize    = tileSize;
    }
 
    /**
     * Renders all tiles visible this frame.
     * Wire this as: {@code pipeline.addLayer(RenderLayerKey.GROUND, tileRenderer::render)}.
     *
     * @param renderer the active renderer for this frame
     */
    public void render(Renderer renderer) {
        Rectangle view = camera.getViewBounds();
 
        // Compute the inclusive tile range that intersects the viewport.
        // floorDiv handles negative world positions correctly.
        int startTileX = Math.floorDiv((int) view.x,           tileSize) - 1;
        int startTileY = Math.floorDiv((int) view.y,           tileSize) - 1;
        int endTileX   = Math.floorDiv((int) view.right(),     tileSize) + 1;
        int endTileY   = Math.floorDiv((int) view.bottom(),    tileSize) + 1;
 
        for (int ty = startTileY; ty <= endTileY; ty++) {
            for (int tx = startTileX; tx <= endTileX; tx++) {
                int id = world.getTileId(tx, ty);
 
                // Tile ID 0 is conventionally empty — skip it.
                if (id <= 0) continue;
 
                Sprite sprite = safeGet(id);
                if (sprite == null) continue;
 
                renderer.drawTile(sprite, tx, ty, tileSize);
            }
        }
    }
 
    /**
     * Returns the sprite for the given tile ID, or {@code null} if the ID is
     * out of range.  Avoids crashing when the world contains tile IDs that
     * don't yet have a sprite registered.
     */
    private Sprite safeGet(int id) {
        try {
            return spriteSheet.get(id);
        } catch (Exception e) {
            return null;
        }
    }
}