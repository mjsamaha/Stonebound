package com.lobsterchops.stonebound.game.ui.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.lobsterchops.stonebound.game.Version;
import com.lobsterchops.stonebound.game.config.ScreenConfig;
import com.lobsterchops.stonebound.game.ui.core.Screen;
import com.lobsterchops.stonebound.game.ui.core.ScreenManager;

/**
 * First screen shown at startup.  Displays the game title for 5 seconds
 * then automatically transitions to {@link DeveloperScreen}.
 */
public class SplashScreen extends Screen {
 
    private static final long DISPLAY_DURATION_NANOS = 5_000_000_000L; // 5 s
 
    private static final Font TITLE_FONT    = new Font("Monospaced", Font.BOLD,  72);
    private static final Font SUBTITLE_FONT = new Font("Monospaced", Font.PLAIN, 18);
 
    private long    elapsed             = 0L;
    private boolean transitionRequested = false;
 
    public SplashScreen(ScreenManager screenManager) {
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
 
        if (!transitionRequested && elapsed >= DISPLAY_DURATION_NANOS) {
            transitionRequested = true;
            screenManager.transitionTo(new DeveloperScreen(screenManager));
        }
    }
 
    @Override
    public void render(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
 
        // Game title
        g2.setFont(TITLE_FONT);
        g2.setColor(Color.WHITE);
        String title = Version.TITLE.toUpperCase();
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (ScreenConfig.WIDTH  - fm.stringWidth(title)) / 2;
        int titleY =  ScreenConfig.HEIGHT / 2;
        g2.drawString(title, titleX, titleY);
 
        // Version subtitle
        g2.setFont(SUBTITLE_FONT);
        g2.setColor(new Color(180, 180, 180));
        String sub = "v" + Version.VERSION;
        FontMetrics sfm = g2.getFontMetrics();
        int subX = (ScreenConfig.WIDTH - sfm.stringWidth(sub)) / 2;
        g2.drawString(sub, subX, titleY + 36);
    }
}