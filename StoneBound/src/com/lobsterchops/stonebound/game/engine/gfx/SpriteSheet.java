package com.lobsterchops.stonebound.game.engine.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private final BufferedImage sheet;
    private final int frameWidth;
    private final int frameHeight;
    private final int columns;

    public SpriteSheet(BufferedImage sheet, int frameWidth, int frameHeight) {
        this.sheet       = sheet;
        this.frameWidth  = frameWidth;
        this.frameHeight = frameHeight;
        this.columns     = sheet.getWidth() / frameWidth;
    }

    /** Returns a {@link Sprite} for the tile at column {@code col}, row {@code row} (0-based). */
    public Sprite get(int col, int row) {
        BufferedImage sub = sheet.getSubimage(
                col * frameWidth, row * frameHeight,
                frameWidth, frameHeight);
        return new Sprite(sub);
    }

    /** Returns a {@link Sprite} for the flat tile index (left-to-right, top-to-bottom). */
    public Sprite get(int index) {
        int col = index % columns;
        int row = index / columns;
        return get(col, row);
    }

    public int getFrameWidth()  { return frameWidth;  }
    public int getFrameHeight() { return frameHeight; }
    public int getColumns()     { return columns; }
    public int getRows()        { return sheet.getHeight() / frameHeight; }
}
