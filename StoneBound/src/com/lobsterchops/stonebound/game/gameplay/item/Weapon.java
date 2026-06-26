package com.lobsterchops.stonebound.game.gameplay.item;

import com.lobsterchops.stonebound.game.config.types.ItemType;

public class Weapon extends Item {

    private final int   damage;
    private final float attackSpeed; // attacks per second

    public Weapon(String id, String displayName, int damage, float attackSpeed) {
        super(id, displayName, ItemType.WEAPON, 1);
        this.damage      = damage;
        this.attackSpeed = attackSpeed;
    }

    public int   getDamage()      { return damage; }
    public float getAttackSpeed() { return attackSpeed; }
}
