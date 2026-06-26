package com.lobsterchops.stonebound.game.engine.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sprite {

    private final BufferedImage image;
    private final int width;
    private final int height;
    /** Pivot offsets – default 0,0 (top-left). */
    private final int originX;
    private final int originY;

    public Sprite(BufferedImage image) {
        this(image, 0, 0);
    }

    public Sprite(BufferedImage image, int originX, int originY) {
        this.image   = image;
        this.width   = image.getWidth();
        this.height  = image.getHeight();
        this.originX = originX;
        this.originY = originY;
    }

    public void draw(Graphics2D g2, int x, int y) {
        g2.drawImage(image, x - originX, y - originY, null);
    }

    public void draw(Graphics2D g2, int x, int y, int w, int h) {
        g2.drawImage(image, x - originX, y - originY, w, h, null);
    }

    public BufferedImage getImage()  { return image; }
    public int getWidth()            { return width;  }
    public int getHeight()           { return height; }
    public int getOriginX()          { return originX; }
    public int getOriginY()          { return originY; }
}
