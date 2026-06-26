package com.lobsterchops.stonebound.game.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Label {

    private String text;
    private int    x, y;
    private Font   font;
    private Color  color;

    public Label(String text, int x, int y, Font font, Color color) {
        this.text  = text;
        this.x     = x;
        this.y     = y;
        this.font  = font;
        this.color = color;
    }

    public void render(Graphics2D g2) {
        g2.setFont(font);
        g2.setColor(color);
        g2.drawString(text, x, y);
    }

    public void setText(String text) { this.text = text; }
    public String getText()          { return text; }
}
