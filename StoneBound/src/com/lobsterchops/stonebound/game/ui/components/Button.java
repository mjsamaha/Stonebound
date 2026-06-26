package com.lobsterchops.stonebound.game.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Button {

    private final String   label;
    private final int      x, y, width, height;
    private final Runnable onClick;
    private boolean hovered = false;

    private static final Font  FONT        = new Font("Arial", Font.BOLD, 14);
    private static final Color BG_NORMAL   = new Color(60, 60, 60);
    private static final Color BG_HOVERED  = new Color(100, 100, 160);
    private static final Color FG          = Color.WHITE;

    public Button(String label, int x, int y, int width, int height, Runnable onClick) {
        this.label   = label;
        this.x       = x;
        this.y       = y;
        this.width   = width;
        this.height  = height;
        this.onClick = onClick;
    }

    public void render(Graphics2D g2) {
        g2.setColor(hovered ? BG_HOVERED : BG_NORMAL);
        g2.fillRoundRect(x, y, width, height, 8, 8);
        g2.setColor(FG);
        g2.setFont(FONT);
        var fm = g2.getFontMetrics();
        int tx = x + (width  - fm.stringWidth(label)) / 2;
        int ty = y + (height + fm.getAscent())         / 2;
        g2.drawString(label, tx, ty);
    }

    public boolean contains(int mx, int my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    public void setHovered(boolean h) { this.hovered = h; }
    public void click()               { onClick.run(); }
}
