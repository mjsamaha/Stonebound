package com.lobsterchops.stonebound.game.engine.util;

public class Timer {

    private final long intervalNanos;
    private long accumulated = 0L;
    private boolean fired = false;

    public Timer(long intervalNanos) {
        this.intervalNanos = intervalNanos;
    }

    /** Feed elapsed nanoseconds from the game loop. */
    public void tick(long elapsedNanos) {
        accumulated += elapsedNanos;
        if (accumulated >= intervalNanos) {
            accumulated -= intervalNanos;
            fired = true;
        }
    }

    /** Returns true once per interval, then resets. */
    public boolean hasFired() {
        if (fired) {
            fired = false;
            return true;
        }
        return false;
    }

    public void reset() {
        accumulated = 0L;
        fired = false;
    }
}
