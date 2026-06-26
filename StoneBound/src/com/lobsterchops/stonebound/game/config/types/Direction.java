package com.lobsterchops.stonebound.game.config.types;

public enum Direction {
    UP, DOWN, LEFT, RIGHT, NONE;

    public Direction opposite() {
        return switch (this) {
            case UP    -> DOWN;
            case DOWN  -> UP;
            case LEFT  -> RIGHT;
            case RIGHT -> LEFT;
            default    -> NONE;
        };
    }
}
