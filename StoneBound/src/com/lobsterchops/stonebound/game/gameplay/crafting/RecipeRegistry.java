package com.lobsterchops.stonebound.game.gameplay.crafting;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class RecipeRegistry {

    private final Map<String, Recipe> recipes = new HashMap<>();

    public void register(Recipe recipe) {
        recipes.put(recipe.getId(), recipe);
    }

    public Recipe get(String id) { return recipes.get(id); }

    public Map<String, Recipe> all() { return Collections.unmodifiableMap(recipes); }
}
