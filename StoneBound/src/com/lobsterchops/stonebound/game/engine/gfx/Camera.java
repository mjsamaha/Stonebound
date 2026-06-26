package com.lobsterchops.stonebound.game.engine.gfx;

import com.lobsterchops.stonebound.game.config.ScreenConfig;
import com.lobsterchops.stonebound.game.engine.math.Vector2;

public class Camera {

    private float offsetX = 0f;
    private float offsetY = 0f;

    private final int viewWidth;
    private final int viewHeight;

    public Camera() {
        this(ScreenConfig.WIDTH, ScreenConfig.HEIGHT);
    }

    public Camera(int viewWidth, int viewHeight) {
        this.viewWidth  = viewWidth;
        this.viewHeight = viewHeight;
    }

    /**
     * Centre the camera on a world-space position.
     * Call once per tick before rendering.
     */
    public void follow(float worldX, float worldY) {
        offsetX = worldX - viewWidth  / 2f;
        offsetY = worldY - viewHeight / 2f;
    }

    public void follow(Vector2 position) {
        follow(position.x, position.y);
    }

    public int getOffsetX() { return (int) offsetX; }
    public int getOffsetY() { return (int) offsetY; }

    public int toScreenX(float worldX) { return (int)(worldX - offsetX); }
    public int toScreenY(float worldY) { return (int)(worldY - offsetY); }
}
