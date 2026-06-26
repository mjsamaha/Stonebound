package com.lobsterchops.stonebound.game.gameplay.item;

import com.lobsterchops.stonebound.game.config.types.ItemType;
import com.lobsterchops.stonebound.game.gameplay.entity.LivingEntity;

public class Consumable extends Item {

    private final int healAmount;

    public Consumable(String id, String displayName, int healAmount) {
        super(id, displayName, ItemType.CONSUMABLE, 16);
        this.healAmount = healAmount;
    }

    public void use(LivingEntity target) {
        target.heal(healAmount);
    }

    public int getHealAmount() { return healAmount; }
}
