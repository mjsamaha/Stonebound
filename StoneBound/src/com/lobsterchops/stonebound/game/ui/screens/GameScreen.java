package com.lobsterchops.stonebound.game.ui.screens;

import java.awt.Graphics2D;

import com.lobsterchops.stonebound.game.core.ServiceLocator;
import com.lobsterchops.stonebound.game.engine.gfx.Renderer;
import com.lobsterchops.stonebound.game.engine.gfx.TileMapRenderer;
import com.lobsterchops.stonebound.game.engine.util.DebugMetrics;
import com.lobsterchops.stonebound.game.engine.util.TmxMapLoader;
import com.lobsterchops.stonebound.game.gameplay.world.MapId;
import com.lobsterchops.stonebound.game.gameplay.world.MapRegistry;
import com.lobsterchops.stonebound.game.gameplay.world.TmxMap;
import com.lobsterchops.stonebound.game.ui.core.Screen;
import com.lobsterchops.stonebound.game.ui.core.ScreenManager;

public class GameScreen extends Screen {

    private final TmxMapLoader tmxMapLoader = new TmxMapLoader();
    private final TileMapRenderer tileMapRenderer = new TileMapRenderer();

    private MapId activeMapId;
    private TmxMap activeMap;

    private MapId pendingMapId;

    public GameScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void onEnter() {
        loadMap(MapRegistry.getStartupMapId());
    }

    @Override
    public void onExit() {
        DebugMetrics metrics = ServiceLocator.resolve(DebugMetrics.class);
        metrics.setMapIdLabel("N/A");
        metrics.resetMapFrameMetrics();

        activeMap = null;
        activeMapId = null;
        pendingMapId = null;
    }

    @Override
    public void update(long elapsedNanos) {
        if (pendingMapId != null) {
            MapId next = pendingMapId;
            pendingMapId = null;
            loadMap(next);
        }
    }

    @Override
    public void render(Graphics2D g2) {
        DebugMetrics metrics = ServiceLocator.resolve(DebugMetrics.class);
        metrics.resetMapFrameMetrics();

        if (activeMap == null) {
            metrics.recordMapRenderWarning("Active map is null.");
            return;
        }

        Renderer renderer = ServiceLocator.resolve(Renderer.class);
        TileMapRenderer.RenderStats stats = tileMapRenderer.renderAllVisibleLayers(renderer, activeMap);

        metrics.setMapTileDrawCalls(stats.getDrawnTiles());
        metrics.setMapTileCulledCount(stats.getCulledTiles());
        metrics.setMapIdLabel(activeMapId != null ? activeMapId.name() : "N/A");

        if (stats.getWarningCount() > 0) {
            metrics.recordMapRenderWarning(stats.getLastWarning());
        }
    }

    /**
     * Batch 6 transition contract: request map swap by logical id.
     * Swap executes in update() to keep lifecycle deterministic.
     */
    public void requestMapTransition(MapId nextMapId) {
        if (nextMapId == null) {
            throw new IllegalArgumentException("nextMapId must not be null");
        }
        pendingMapId = nextMapId;
    }

    /**
     * Reload current map from registry path (useful for dev hot-reload hooks).
     */
    public void reloadActiveMap() {
        if (activeMapId == null) {
            throw new IllegalStateException("No active map id to reload.");
        }
        pendingMapId = activeMapId;
    }

    public MapId getActiveMapId() {
        return activeMapId;
    }

    public TmxMap getActiveMap() {
        return activeMap;
    }

    private void loadMap(MapId mapId) {
        if (mapId == null) {
            throw new IllegalArgumentException("mapId must not be null");
        }
        if (!MapRegistry.has(mapId)) {
            throw new IllegalArgumentException("Map id is not registered: " + mapId);
        }

        String tmxPath = MapRegistry.pathFor(mapId);
        TmxMap loaded = tmxMapLoader.load(tmxPath);

        activeMapId = mapId;
        activeMap = loaded;

        DebugMetrics metrics = ServiceLocator.resolve(DebugMetrics.class);
        metrics.setMapIdLabel(activeMapId.name());
        metrics.resetMapFrameMetrics();
    }
}