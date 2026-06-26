package com.lobsterchops.stonebound.game.gameplay.world;

import java.util.HashMap;
import java.util.Map;

public class World {

    private final Map<Long, Chunk> chunks = new HashMap<>();

    /** Returns the chunk at the given chunk coordinates, loading/generating if needed. */
    public Chunk getChunk(int cx, int cy) {
        long key = packKey(cx, cy);
        return chunks.computeIfAbsent(key, k -> new Chunk(cx, cy));
    }

    public void setTileId(int worldX, int worldY, int id) {
        int cx = Math.floorDiv(worldX, Chunk.SIZE);
        int cy = Math.floorDiv(worldY, Chunk.SIZE);
        int lx = Math.floorMod(worldX, Chunk.SIZE);
        int ly = Math.floorMod(worldY, Chunk.SIZE);
        getChunk(cx, cy).setTileId(lx, ly, id);
    }

    public void update() {
        // TODO: tick active entities, spawners, etc.
    }

    private static long packKey(int cx, int cy) {
        return ((long) cx << 32) | (cy & 0xFFFFFFFFL);
    }
}
