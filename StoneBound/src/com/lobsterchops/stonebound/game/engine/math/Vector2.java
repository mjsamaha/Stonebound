package com.lobsterchops.stonebound.game.engine.math;

public class Vector2 {

    public float x;
    public float y;

    public Vector2() { this(0f, 0f); }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 sub(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 scale(float s) {
        return new Vector2(x * s, y * s);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2 normalize() {
        float len = length();
        if (len == 0f) return new Vector2();
        return new Vector2(x / len, y / len);
    }

    public float dot(Vector2 other) {
        return x * other.x + y * other.y;
    }

    public float distanceTo(Vector2 other) {
        return sub(other).length();
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    @Override
    public String toString() {
        return "Vector2(" + x + ", " + y + ")";
    }
}
