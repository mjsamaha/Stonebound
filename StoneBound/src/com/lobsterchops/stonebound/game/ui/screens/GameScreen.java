package com.lobsterchops.stonebound.game.ui.screens;

import java.awt.Graphics2D;

import com.lobsterchops.stonebound.game.ui.core.Screen;
import com.lobsterchops.stonebound.game.ui.core.ScreenManager;

public class GameScreen extends Screen {

    public GameScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void onEnter() {
        // TODO: start world tick, register HUD render layers
    }

    @Override
    public void onExit() {
        // TODO: pause world, deregister HUD layers
    }

    @Override
    public void update(long elapsedNanos) {
        // TODO: world.update(), player.update(), etc.
    }

    @Override
    public void render(Graphics2D g2) {
        // JPanel background is already black — nothing needed until
        // world and entity rendering are wired in.
    }
}
