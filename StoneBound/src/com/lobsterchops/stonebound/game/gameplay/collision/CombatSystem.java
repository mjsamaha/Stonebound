package com.lobsterchops.stonebound.game.gameplay.collision;

import com.lobsterchops.stonebound.game.gameplay.entity.LivingEntity;

public class CombatSystem {

    public void resolve(LivingEntity attacker, Attack attack, LivingEntity target, int defense) {
        int finalDamage = DamageCalculator.calculate(attack.getBaseDamage(), defense);
        target.damage(finalDamage);
    }
}
