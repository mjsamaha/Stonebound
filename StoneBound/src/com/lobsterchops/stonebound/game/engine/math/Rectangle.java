package com.lobsterchops.stonebound.game.engine.math;

public class Rectangle {

    public float x;
    public float y;
    public float width;
    public float height;

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(Rectangle other) {
        return x < other.x + other.width
            && x + width  > other.x
            && y < other.y + other.height
            && y + height > other.y;
    }

    public boolean contains(float px, float py) {
        return px >= x && px <= x + width
            && py >= y && py <= y + height;
    }

    public float right()  { return x + width; }
    public float bottom() { return y + height; }

    public Vector2 center() {
        return new Vector2(x + width / 2f, y + height / 2f);
    }

    @Override
    public String toString() {
        return "Rect(" + x + "," + y + " " + width + "x" + height + ")";
    }
}
