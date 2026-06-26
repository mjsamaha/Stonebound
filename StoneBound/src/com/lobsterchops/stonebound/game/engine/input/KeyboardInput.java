package com.lobsterchops.stonebound.game.engine.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class KeyboardInput extends KeyAdapter {

    private final Set<Integer> down         = Collections.synchronizedSet(new HashSet<>());
    private final Set<Integer> justPressed  = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void keyPressed(KeyEvent e) {
        if (!down.contains(e.getKeyCode())) {
            justPressed.add(e.getKeyCode());
        }
        down.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        down.remove(e.getKeyCode());
    }

    public boolean isDown(int keyCode)        { return down.contains(keyCode); }
    public boolean isJustPressed(int keyCode) { return justPressed.contains(keyCode); }

    /** Must be called at the end of each tick to clear single-frame flags. */
    public void flush() { justPressed.clear(); }
}
