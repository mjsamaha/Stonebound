package com.lobsterchops.stonebound.game.gameplay.item;

import com.lobsterchops.stonebound.game.config.types.ItemType;

public abstract class Item {

    protected final String   id;
    protected final String   displayName;
    protected final ItemType type;
    protected final int      maxStack;

    protected Item(String id, String displayName, ItemType type, int maxStack) {
        this.id          = id;
        this.displayName = displayName;
        this.type        = type;
        this.maxStack    = maxStack;
    }

    public String   getId()          { return id; }
    public String   getDisplayName() { return displayName; }
    public ItemType getType()        { return type; }
    public int      getMaxStack()    { return maxStack; }
}
