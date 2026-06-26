package com.lobsterchops.stonebound.game.gameplay.world;

public class Tile {

    private final int  id;
    private final boolean solid;
    private final boolean opaque;

    public Tile(int id, boolean solid, boolean opaque) {
        this.id     = id;
        this.solid  = solid;
        this.opaque = opaque;
    }

    public int     getId()     { return id; }
    public boolean isSolid()   { return solid; }
    public boolean isOpaque()  { return opaque; }
}
