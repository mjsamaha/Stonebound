package com.lobsterchops.stonebound.game.ui.core;

import java.awt.Graphics2D;

/**
 * Base class for every full-screen game state driven by {@link ScreenManager}.
 *
 * <p>Every concrete Screen MUST call {@code super(screenManager)} so the
 * back-reference is set — this is what allows screens to trigger their own
 * transitions via {@code screenManager.transitionTo(...)}.
 *
 * <p>Screens never know about fading; the ScreenManager composites the
 * black overlay on top after delegating to the active screen's render pass.
 */
public abstract class Screen {
 
    /** Back-reference so screens can trigger their own transitions. */
    protected final ScreenManager screenManager;
 
    protected Screen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }
 
    /**
     * Called once per game tick.
     *
     * @param elapsedNanos nanoseconds since the previous tick
     */
    public abstract void update(long elapsedNanos);
 
    /**
     * Called once per render pass.  The Graphics2D context is already
     * configured by GamePanel (double-buffered, background cleared).
     * Do NOT call g2.dispose() — GamePanel owns the context.
     */
    public abstract void render(Graphics2D g2);
 
    /** Called once when this screen becomes active. Override to reset state. */
    public void onEnter() {}
 
    /** Called once just before this screen is replaced. Override to clean up. */
    public void onExit() {}
}