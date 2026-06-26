package com.lobsterchops.stonebound.game.gameplay.entity;

import com.lobsterchops.stonebound.game.config.types.EntityType;

public abstract class LivingEntity extends Entity {

    protected int maxHealth;
    protected int health;
    protected boolean dead = false;

    protected LivingEntity(EntityType type, float x, float y, int maxHealth) {
        super(type, x, y);
        this.maxHealth = maxHealth;
        this.health    = maxHealth;
    }

    public void damage(int amount) {
        health = Math.max(0, health - amount);
        if (health == 0) onDeath();
    }

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    protected void onDeath() {
        dead = true;
        active = false;
    }

    public boolean isDead()    { return dead; }
    public int getHealth()     { return health; }
    public int getMaxHealth()  { return maxHealth; }
}
