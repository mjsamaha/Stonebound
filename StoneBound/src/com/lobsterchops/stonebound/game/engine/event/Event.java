package com.lobsterchops.stonebound.game.engine.event;

public abstract class Event {

    private boolean consumed = false;

    /** Mark the event as handled so other listeners can skip it. */
    public void consume() { consumed = true; }

    public boolean isConsumed() { return consumed; }
}
