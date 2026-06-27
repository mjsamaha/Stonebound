package com.lobsterchops.stonebound.game.engine.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.lobsterchops.stonebound.game.gameplay.world.TileLayer;
import com.lobsterchops.stonebound.game.gameplay.world.TilesetDef;
import com.lobsterchops.stonebound.game.gameplay.world.TmxMap;

/**
 * Renders TMX tile layers with camera culling.
 * Scope: orthogonal tile maps with static tile layers.
 */
public final class TileMapRenderer {

    private final Map<String, BufferedImage> imageCache = new HashMap<>();
    private final Map<String, Boolean> failedImages = new HashMap<>();

    public RenderStats renderAllVisibleLayers(Renderer renderer, TmxMap map) {
        if (renderer == null || map == null) return new RenderStats(0, 0, 0, "");

        Camera camera = renderer.getCamera();
        if (camera == null) {
            return new RenderStats(0, 0, 1, "Renderer camera is null.");
        }

        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();
        if (tileWidth <= 0 || tileHeight <= 0) {
            return new RenderStats(0, 0, 1, "Map tile size is invalid.");
        }

        int cameraX = camera.getX();
        int cameraY = camera.getY();

        int viewportW = renderer.getBufferWidth();
        int viewportH = renderer.getBufferHeight();

        // Add a 1-tile border to reduce edge pop when camera moves.
        int minTileX = Math.max(0, (cameraX / tileWidth) - 1);
        int minTileY = Math.max(0, (cameraY / tileHeight) - 1);
        int maxTileX = Math.min(map.getWidthInTiles() - 1, ((cameraX + viewportW) / tileWidth) + 1);
        int maxTileY = Math.min(map.getHeightInTiles() - 1, ((cameraY + viewportH) / tileHeight) + 1);

        int drawn = 0;
        int culled = 0;
        int warnings = 0;
        String lastWarning = "";

        for (TileLayer layer : map.getLayers()) {
            if (layer == null || !layer.isVisible()) continue;

            int layerW = Math.max(0, layer.getWidth());
            int layerH = Math.max(0, layer.getHeight());
            int totalTiles = layerW * layerH;
            int visibleTiles = Math.max(0, (maxTileX - minTileX + 1)) * Math.max(0, (maxTileY - minTileY + 1));
            culled += Math.max(0, totalTiles - visibleTiles);

            for (int y = minTileY; y <= maxTileY; y++) {
                for (int x = minTileX; x <= maxTileX; x++) {
                    int gid = layer.getGid(x, y);
                    if (gid == 0) continue;

                    TilesetDef tileset = map.findTilesetForGid(gid);
                    if (tileset == null) {
                        warnings++;
                        lastWarning = "No tileset for gid=" + gid;
                        continue;
                    }

                    BufferedImage sheet = loadTilesetImage(tileset.getImageSource());
                    if (sheet == null) {
                        warnings++;
                        lastWarning = "Tileset image unavailable: " + tileset.getImageSource();
                        continue;
                    }

                    int localId = gid - tileset.getFirstGid();
                    if (localId < 0) {
                        warnings++;
                        lastWarning = "Negative localId for gid=" + gid;
                        continue;
                    }

                    int cols = tileset.getColumns();
                    if (cols <= 0) {
                        warnings++;
                        lastWarning = "Tileset columns invalid for " + tileset.getImageSource();
                        continue;
                    }

                    int srcCol = localId % cols;
                    int srcRow = localId / cols;

                    int tw = tileset.getTileWidth();
                    int th = tileset.getTileHeight();
                    int spacing = tileset.getSpacing();
                    int margin = tileset.getMargin();

                    int srcX = margin + srcCol * (tw + spacing);
                    int srcY = margin + srcRow * (th + spacing);

                    if (srcX < 0 || srcY < 0 || srcX + tw > sheet.getWidth() || srcY + th > sheet.getHeight()) {
                        warnings++;
                        lastWarning = "Tile source rect out of bounds for gid=" + gid;
                        continue;
                    }

                    int worldX = x * tileWidth;
                    int worldY = y * tileHeight;
                    int screenX = camera.toScreenX(worldX);
                    int screenY = camera.toScreenY(worldY);

                    if (renderer.getRaw() == null) {
                        warnings++;
                        lastWarning = "Renderer raw graphics is null (outside begin/end frame).";
                        continue;
                    }

                    renderer.getRaw().drawImage(
                            sheet,
                            screenX, screenY, screenX + tileWidth, screenY + tileHeight,
                            srcX, srcY, srcX + tw, srcY + th,
                            null
                    );
                    drawn++;
                }
            }
        }

        return new RenderStats(drawn, culled, warnings, lastWarning);
    }

    private BufferedImage loadTilesetImage(String path) {
        if (path == null || path.isBlank()) return null;

        BufferedImage cached = imageCache.get(path);
        if (cached != null) return cached;
        if (Boolean.TRUE.equals(failedImages.get(path))) return null;

        try {
            BufferedImage img = ImageIO.read(new File(path));
            if (img != null) {
                imageCache.put(path, img);
                return img;
            }
            failedImages.put(path, true);
            return null;
        } catch (Exception ex) {
            failedImages.put(path, true);
            return null;
        }
    }

    public static final class RenderStats {
        private final int drawnTiles;
        private final int culledTiles;
        private final int warningCount;
        private final String lastWarning;

        public RenderStats(int drawnTiles, int culledTiles, int warningCount, String lastWarning) {
            this.drawnTiles = drawnTiles;
            this.culledTiles = culledTiles;
            this.warningCount = warningCount;
            this.lastWarning = (lastWarning == null) ? "" : lastWarning;
        }

        public int getDrawnTiles() {
            return drawnTiles;
        }

        public int getCulledTiles() {
            return culledTiles;
        }

        public int getWarningCount() {
            return warningCount;
        }

        public String getLastWarning() {
            return lastWarning;
        }
    }
}
