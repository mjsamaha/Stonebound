package com.lobsterchops.stonebound.game.gameplay.collision;

public class Attack {

    private final String sourceId;
    private final int    baseDamage;
    private final float  knockback;

    public Attack(String sourceId, int baseDamage, float knockback) {
        this.sourceId   = sourceId;
        this.baseDamage = baseDamage;
        this.knockback  = knockback;
    }

    public String getSourceId()   { return sourceId; }
    public int    getBaseDamage() { return baseDamage; }
    public float  getKnockback() { return knockback; }
}
