package com.lobsterchops.stonebound.game.ui.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Panel {

    private final int   x, y, width, height;
    private       Color background;
    private final List<Button> buttons = new ArrayList<>();
    private final List<Label>  labels  = new ArrayList<>();

    public Panel(int x, int y, int width, int height, Color background) {
        this.x          = x;
        this.y          = y;
        this.width      = width;
        this.height     = height;
        this.background = background;
    }

    public void render(Graphics2D g2) {
        g2.setColor(background);
        g2.fillRoundRect(x, y, width, height, 12, 12);
        labels.forEach(l -> l.render(g2));
        buttons.forEach(b -> b.render(g2));
    }

    public void addButton(Button b) { buttons.add(b); }
    public void addLabel(Label l)   { labels.add(l); }
}
