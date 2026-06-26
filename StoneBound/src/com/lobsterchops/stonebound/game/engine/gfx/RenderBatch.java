package com.lobsterchops.stonebound.game.engine.gfx;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


public class RenderBatch {

    private record DrawCall(Sprite sprite, int x, int y) {}

    private final List<DrawCall> calls = new ArrayList<>();

    public void submit(Sprite sprite, int x, int y) {
        calls.add(new DrawCall(sprite, x, y));
    }

    public void flush(Graphics2D g2) {
        for (DrawCall dc : calls) {
            dc.sprite().draw(g2, dc.x(), dc.y());
        }
        calls.clear();
    }

    public void clear() { calls.clear(); }
}
