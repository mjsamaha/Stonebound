package com.lobsterchops.stonebound.game.gameplay.crafting;

import com.lobsterchops.stonebound.game.gameplay.inventory.Inventory;

public class CraftingSystem {

    private final RecipeRegistry registry;

    public CraftingSystem(RecipeRegistry registry) {
        this.registry = registry;
    }

    public boolean canCraft(String recipeId, Inventory inventory) {
        Recipe recipe = registry.get(recipeId);
        if (recipe == null) return false;
        // TODO: check inventory has required stacks
        return false;
    }

    public boolean craft(String recipeId, Inventory inventory) {
        if (!canCraft(recipeId, inventory)) return false;
        // TODO: consume ingredients, add result
        return true;
    }
}
