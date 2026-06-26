package com.lobsterchops.stonebound.game.gameplay.item;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {

    private final Map<String, Item> items = new HashMap<>();

    public void register(Item item) {
        if (items.containsKey(item.getId()))
            throw new IllegalStateException("Duplicate item id: " + item.getId());
        items.put(item.getId(), item);
    }

    public Item get(String id) {
        Item item = items.get(id);
        if (item == null) throw new IllegalArgumentException("Unknown item: " + id);
        return item;
    }

    public Map<String, Item> all() { return Collections.unmodifiableMap(items); }
}
