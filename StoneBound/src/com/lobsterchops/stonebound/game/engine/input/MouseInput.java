package com.lobsterchops.stonebound.game.engine.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

    private int mouseX = 0;
    private int mouseY = 0;
    private boolean leftDown  = false;
    private boolean rightDown = false;
    private boolean leftJustPressed  = false;
    private boolean rightJustPressed = false;

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) { leftDown = true;  leftJustPressed  = true; }
        if (e.getButton() == MouseEvent.BUTTON3) { rightDown = true; rightJustPressed = true; }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) leftDown  = false;
        if (e.getButton() == MouseEvent.BUTTON3) rightDown = false;
    }

    public int     getX()               { return mouseX; }
    public int     getY()               { return mouseY; }
    public boolean isLeftDown()         { return leftDown; }
    public boolean isRightDown()        { return rightDown; }
    public boolean isLeftJustPressed()  { return leftJustPressed; }
    public boolean isRightJustPressed() { return rightJustPressed; }

    /** Clear single-frame pressed flags. Call at end of tick. */
    public void flush() {
        leftJustPressed  = false;
        rightJustPressed = false;
    }
}
