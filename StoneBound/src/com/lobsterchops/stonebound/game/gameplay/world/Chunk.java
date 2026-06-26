package com.lobsterchops.stonebound.game.gameplay.world;

public class Chunk {

    public static final int SIZE = 16; // tiles per side

    private final int chunkX;
    private final int chunkY;
    private final int[][] tileIds = new int[SIZE][SIZE];

    public Chunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
    }

    public int getTileId(int localX, int localY) {
        return tileIds[localX][localY];
    }

    public void setTileId(int localX, int localY, int id) {
        tileIds[localX][localY] = id;
    }

    public int getChunkX() { return chunkX; }
    public int getChunkY() { return chunkY; }
}
