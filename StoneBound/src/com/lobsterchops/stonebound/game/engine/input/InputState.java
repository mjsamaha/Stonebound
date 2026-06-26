package com.lobsterchops.stonebound.game.engine.input;

import java.util.EnumMap;
import java.util.Map;


public class InputState {

    private final Map<InputAction, Boolean> held    = new EnumMap<>(InputAction.class);
    private final Map<InputAction, Boolean> pressed = new EnumMap<>(InputAction.class);

    InputState(Map<InputAction, Boolean> held, Map<InputAction, Boolean> pressed) {
        this.held.putAll(held);
        this.pressed.putAll(pressed);
    }

    /** True every tick the action key is down. */
    public boolean isHeld(InputAction action) {
        return held.getOrDefault(action, false);
    }

    /** True only on the first tick the action key is pressed. */
    public boolean isPressed(InputAction action) {
        return pressed.getOrDefault(action, false);
    }
}
