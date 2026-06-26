package com.lobsterchops.stonebound.game.ui.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.lobsterchops.stonebound.game.config.ScreenConfig;
import com.lobsterchops.stonebound.game.ui.core.Screen;
import com.lobsterchops.stonebound.game.ui.core.ScreenManager;

/**
 * Developer / studio credit screen shown for 5 seconds after the splash,
 * then automatically transitions to {@link MenuScreen}.
 */
public class DeveloperScreen extends Screen {
 
	private static final String DEVELOPER_NAME = "LOBSTER CHOPS";
 
    private static final Font CREDIT_FONT = new Font("Monospaced", Font.BOLD, 16);
    private static final Font NAME_FONT   = new Font("Monospaced", Font.BOLD,  36);
 
    private long    elapsed             = 0L;
    private boolean transitionRequested = false;
 
    public DeveloperScreen(ScreenManager screenManager) {
        super(screenManager);
    }
 
    @Override
    public void onEnter() {
        elapsed             = 0L;
        transitionRequested = false;
    }
 
    @Override
    public void update(long elapsedNanos) {
        elapsed += elapsedNanos;
 
        if (!transitionRequested && elapsed >= ScreenConfig.DISPLAY_DURATION) {
            transitionRequested = true;
            screenManager.transitionTo(new MenuScreen(screenManager));
        }
    }
 
    @Override
    public void render(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
 
        int centerX = ScreenConfig.WIDTH  / 2;
        int centerY = ScreenConfig.HEIGHT / 2;
 
        // "A game by" label
        g2.setFont(CREDIT_FONT);
        g2.setColor(new Color(160, 160, 160));
        String label = "A game by";
        FontMetrics lfm = g2.getFontMetrics();
        g2.drawString(label, centerX - lfm.stringWidth(label) / 2, centerY - 24);
 
        // Developer name
        g2.setFont(NAME_FONT);
        g2.setColor(Color.WHITE);
        String name = DEVELOPER_NAME;
        FontMetrics nfm = g2.getFontMetrics();
        g2.drawString(name, centerX - nfm.stringWidth(name) / 2, centerY + 20);
        
        // Music credit
        g2.setFont(CREDIT_FONT);
        g2.setColor(Color.WHITE);
        String musicCredit = "Music by: Trevor Lentz";
        FontMetrics mfm = g2.getFontMetrics();
        g2.drawString(musicCredit, centerX - mfm.stringWidth(musicCredit) / 2, centerY + 60);
    }
}
 