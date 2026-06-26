package com.lobsterchops.stonebound.game.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;

public class InputManager {

    private final KeyboardInput keyboard;
    private final MouseInput    mouse;

    /** Maps logical action → primary key code. */
    private final Map<InputAction, Integer> keyBindings = new EnumMap<>(InputAction.class);

    public InputManager(KeyboardInput keyboard, MouseInput mouse) {
        this.keyboard = keyboard;
        this.mouse    = mouse;
        loadDefaultBindings();
    }

    private void loadDefaultBindings() {
        keyBindings.put(InputAction.MOVE_UP,        KeyEvent.VK_W);
        keyBindings.put(InputAction.MOVE_DOWN,      KeyEvent.VK_S);
        keyBindings.put(InputAction.MOVE_LEFT,      KeyEvent.VK_A);
        keyBindings.put(InputAction.MOVE_RIGHT,     KeyEvent.VK_D);
        
        keyBindings.put(InputAction.ATTACK,         MouseEvent.BUTTON1); // Left mouse button for attack
        keyBindings.put(InputAction.INTERACT,       KeyEvent.VK_E);
        keyBindings.put(InputAction.OPEN_INVENTORY, KeyEvent.VK_I);
        keyBindings.put(InputAction.OPEN_CRAFTING,  KeyEvent.VK_C);
        
        keyBindings.put(InputAction.PAUSE,          KeyEvent.VK_ESCAPE);
        keyBindings.put(InputAction.QUIT, 			KeyEvent.VK_ESCAPE);
        keyBindings.put(InputAction.CONFIRM,        KeyEvent.VK_ENTER);
        keyBindings.put(InputAction.DEBUG, KeyEvent.VK_F3); // F3 for debug
    }

    public void rebind(InputAction action, int keyCode) {
        keyBindings.put(action, keyCode);
    }

    /** Build and return this tick's input snapshot. */
    public InputState poll() {
        Map<InputAction, Boolean> held    = new EnumMap<>(InputAction.class);
        Map<InputAction, Boolean> pressed = new EnumMap<>(InputAction.class);

        for (InputAction action : InputAction.values()) {
            Integer key = keyBindings.get(action);
            if (key == null) continue;
            held.put(action,    keyboard.isDown(key));
            pressed.put(action, keyboard.isJustPressed(key));
        }

        return new InputState(held, pressed);
    }

    /** Must be called at the end of every tick. */
    public void flush() {
        keyboard.flush();
        mouse.flush();
    }

    public KeyboardInput getKeyboard() { return keyboard; }
    public MouseInput    getMouse()    { return mouse; }
}
