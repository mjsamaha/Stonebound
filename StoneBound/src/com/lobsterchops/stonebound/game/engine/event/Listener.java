package com.lobsterchops.stonebound.game.engine.event;

@FunctionalInterface
public interface Listener<E extends Event> {
    void onEvent(E event);
}
