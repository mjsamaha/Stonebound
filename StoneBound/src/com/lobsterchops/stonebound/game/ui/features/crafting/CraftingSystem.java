package com.lobsterchops.stonebound.game.ui.features.crafting;

public class CraftingSystem {

    private boolean open = false;

    public void open()  { open = true;  }
    public void close() { open = false; }
    public boolean isOpen() { return open; }
}
