package com.lobsterchops.stonebound.game.gameplay.crafting;

import java.util.List;

import com.lobsterchops.stonebound.game.gameplay.inventory.InventoryStack;


public class Recipe {

    private final String              id;
    private final List<InventoryStack> ingredients;
    private final InventoryStack      result;

    public Recipe(String id, List<InventoryStack> ingredients, InventoryStack result) {
        this.id          = id;
        this.ingredients = List.copyOf(ingredients);
        this.result      = result;
    }

    public String               getId()          { return id; }
    public List<InventoryStack> getIngredients() { return ingredients; }
    public InventoryStack       getResult()      { return result; }
}
