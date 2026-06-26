package com.lobsterchops.stonebound.game.engine.math;

import java.util.Random;

public final class RandomUtil {

    private static final Random RNG = new Random();

    private RandomUtil() {}

    public static void setSeed(long seed) { RNG.setSeed(seed); }

    public static int nextInt(int bound) { return RNG.nextInt(bound); }

    public static int range(int min, int max) { return min + RNG.nextInt(max - min + 1); }

    public static float nextFloat() { return RNG.nextFloat(); }

    public static boolean chance(float probability) { return RNG.nextFloat() < probability; }
}
