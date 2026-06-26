package com.lobsterchops.stonebound.game.engine.math;

public final class MathUtil {

    private MathUtil() {}

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * clamp(t, 0f, 1f);
    }

    public static boolean approxEqual(float a, float b, float epsilon) {
        return Math.abs(a - b) < epsilon;
    }

    public static int toTile(float worldCoord, int tileSize) {
        return (int) Math.floor(worldCoord / tileSize);
    }
}
