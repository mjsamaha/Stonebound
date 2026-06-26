package com.lobsterchops.stonebound.game.gameplay.collision;

public final class DamageCalculator {

    private DamageCalculator() {}

    /**
     * Compute final damage after defense reduction.
     * Formula: max(1, baseDamage - defense).
     */
    public static int calculate(int baseDamage, int defense) {
        return Math.max(1, baseDamage - defense);
    }
}
