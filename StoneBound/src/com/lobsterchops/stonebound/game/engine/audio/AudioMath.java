package com.lobsterchops.stonebound.game.engine.audio;

public final class AudioMath {

    private AudioMath() {}

    /** Converts a linear 0-1 volume to decibel gain for javax.sound. */
    public static float linearToDecibels(float linear) {
        if (linear <= 0f) return -80f;
        return 20f * (float) Math.log10(linear);
    }

    /** Clamps a volume value to [0, 1]. */
    public static float clampVolume(float v) {
        return Math.max(0f, Math.min(1f, v));
    }
}
