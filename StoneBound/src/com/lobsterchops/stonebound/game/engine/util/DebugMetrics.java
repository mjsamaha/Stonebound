package com.lobsterchops.stonebound.game.engine.util;

public class DebugMetrics {

    private int fps;
    private int mapTileDrawCalls;
    private int mapTileCulledCount;
    private String mapIdLabel = "N/A";

    private int mapRenderWarningCount;
    private String mapLastWarning = "";

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getMapTileDrawCalls() {
        return mapTileDrawCalls;
    }

    public void setMapTileDrawCalls(int mapTileDrawCalls) {
        this.mapTileDrawCalls = Math.max(0, mapTileDrawCalls);
    }

    public int getMapTileCulledCount() {
        return mapTileCulledCount;
    }

    public void setMapTileCulledCount(int mapTileCulledCount) {
        this.mapTileCulledCount = Math.max(0, mapTileCulledCount);
    }

    public String getMapIdLabel() {
        return mapIdLabel;
    }

    public void setMapIdLabel(String mapIdLabel) {
        this.mapIdLabel = (mapIdLabel == null || mapIdLabel.isBlank()) ? "N/A" : mapIdLabel;
    }

    public int getMapRenderWarningCount() {
        return mapRenderWarningCount;
    }

    public String getMapLastWarning() {
        return mapLastWarning;
    }

    public void recordMapRenderWarning(String warning) {
        mapRenderWarningCount++;
        mapLastWarning = (warning == null) ? "" : warning;
    }

    public void clearMapRenderWarnings() {
        mapRenderWarningCount = 0;
        mapLastWarning = "";
    }

    public void resetMapRenderCounters() {
        this.mapTileDrawCalls = 0;
        this.mapTileCulledCount = 0;
    }

    public void resetMapFrameMetrics() {
        resetMapRenderCounters();
        clearMapRenderWarnings();
    }
}