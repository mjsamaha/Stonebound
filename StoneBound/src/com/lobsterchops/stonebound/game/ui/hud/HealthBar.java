package com.lobsterchops.stonebound.game.ui.hud;

import java.awt.Color;
import java.awt.Graphics2D;

import com.lobsterchops.stonebound.game.ui.core.UILayer;


public class HealthBar implements UILayer {

    private int currentHp;
    private int maxHp;

    private static final int X = 10, Y = 10, W = 120, H = 12;

    public void setHealth(int current, int max) {
        this.currentHp = current;
        this.maxHp     = max;
    }

    @Override public void update() { /* driven by player state */ }

    @Override
    public void render(Graphics2D g2) {
        // background
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(X, Y, W, H);
        // fill
        if (maxHp > 0) {
            float pct = (float) currentHp / maxHp;
            g2.setColor(Color.RED);
            g2.fillRect(X, Y, (int)(W * pct), H);
        }
        // border
        g2.setColor(Color.WHITE);
        g2.drawRect(X, Y, W, H);
    }
}
