package com.lobsterchops.stonebound.game.ui.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import com.lobsterchops.stonebound.game.Version;
import com.lobsterchops.stonebound.game.config.ScreenConfig;
import com.lobsterchops.stonebound.game.core.ServiceLocator;
import com.lobsterchops.stonebound.game.engine.audio.AudioService;
import com.lobsterchops.stonebound.game.engine.audio.content.SoundIds;
import com.lobsterchops.stonebound.game.engine.input.InputAction;
import com.lobsterchops.stonebound.game.engine.input.InputState;
import com.lobsterchops.stonebound.game.ui.core.Screen;
import com.lobsterchops.stonebound.game.ui.core.ScreenManager;

/**
 * Main menu screen.  Renders a title and a blinking "Press ENTER" prompt.
 *
 * <p>Hook {@link #onKeyPressed(int)} into InputManager once that layer
 * dispatches key events to the active screen.
 */
public class MenuScreen extends Screen {
 
    private static final Font  TITLE_FONT        = new Font("Monospaced", Font.BOLD,  64);
    private static final Font  PROMPT_FONT        = new Font("Monospaced", Font.PLAIN, 20);
 
    private long elapsed = 0L;
 
    public MenuScreen(ScreenManager screenManager) {
        super(screenManager);
    }
 
    @Override
    public void onEnter() {
        elapsed = 0L;
    }
 
    @Override
    public void update(long elapsedNanos) {
        elapsed += elapsedNanos;
    }
    
    public void handleInput(InputState input) {
    	
    	AudioService audio = ServiceLocator.resolve(AudioService.class);
    	
    	if (input.isPressed(InputAction.CONFIRM)) {
    		audio.play(SoundIds.UI_CONFIRM);
    		audio.stop(SoundIds.PRE_LOADER_MUSIC);
    		audio.play(SoundIds.GAMEPLAY_MUSIC_ONE);
    		screenManager.transitionTo(new StoryScreen(screenManager));
    	}
    }
 
    @Override
    public void render(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
 
        int centerX = ScreenConfig.WIDTH  / 2;
        int centerY = ScreenConfig.HEIGHT / 2;
 
        // Game title
        g2.setFont(TITLE_FONT);
        g2.setColor(Color.WHITE);
        String title = Version.TITLE.toUpperCase();
        FontMetrics tfm = g2.getFontMetrics();
        g2.drawString(title, centerX - tfm.stringWidth(title) / 2, centerY - 60);
 
        // Blinking prompt
        if ((elapsed % ScreenConfig.BLINK_PERIOD_NANOS) < (ScreenConfig.BLINK_PERIOD_NANOS / 2)) {
            g2.setFont(PROMPT_FONT);
            g2.setColor(new Color(200, 200, 200));
            String prompt = "PRESS  ENTER  TO  START";
            FontMetrics pfm = g2.getFontMetrics();
            g2.drawString(prompt, centerX - pfm.stringWidth(prompt) / 2, centerY + 40);
        }
        
        // Static ESC hint - always visible
        g2.setFont(PROMPT_FONT);
        g2.setColor(new Color(120, 120, 120));
        String quit = "ESC TO QUIT";
        FontMetrics qfm = g2.getFontMetrics();
        g2.drawString(quit,  centerX - qfm.stringWidth(quit) / 2, centerY + 80);
    }
 
    /** @deprecated Unconnected stub — input is dispatched via handleInput(InputState). */
    @Deprecated
    public void onKeyPressed(int keyCode) {}
}