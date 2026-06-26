package com.lobsterchops.stonebound.game.ui.core;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import com.lobsterchops.stonebound.game.config.ScreenConfig;

/**
 * Owns the active {@link Screen} and drives FADE-OUT → swap → FADE-IN
 * transitions between screens.
 *
 * <p><b>Bootstrap order</b> (used in GamePanel.setupGame):
 * <pre>
 *   ScreenManager sm = new ScreenManager();            // sentinel active
 *   ServiceLocator.register(ScreenManager.class, sm);
 *   sm.setInitialScreen(new SplashScreen(sm));         // real first screen
 * </pre>
 * This two-step avoids the circular dependency: SplashScreen needs a
 * ScreenManager reference, but ScreenManager needs a Screen to construct.
 *
 * <p><b>Transition flow:</b>
 * <ol>
 *   <li>Any code calls {@link #transitionTo(Screen)}.
 *   <li>Manager enters FADE_OUT — alpha ramps 0 → 1 over 400 ms.
 *   <li>At full black: {@link Screen#onExit()} old, swap, {@link Screen#onEnter()} new.
 *   <li>Manager enters FADE_IN — alpha ramps 1 → 0 over 400 ms.
 *   <li>Back to IDLE.
 * </ol>
 *
 * <p>The active screen's {@link Screen#update(long)} keeps running during
 * fades so animations and timers do not stall.
 *
 * <p><b>Timing:</b> {@link #tick()} is self-clocking via {@link System#nanoTime()}
 * and is intended to be wired as the {@code updateTick} Runnable in
 * {@link com.lobsterchops.stonebound.game.core.GameLoop}, which passes no
 * elapsed-time argument to its callbacks.
 */
public class ScreenManager {
 
 
    /** Duration of each fade half (out or in), in nanoseconds (400 ms). */
    private static final long FADE_DURATION_NANOS = 400_000_000L;
 
 
    private enum TransitionState { IDLE, FADE_OUT, FADE_IN }
 
    private Screen          activeScreen;
    private Screen          pendingScreen;
    private TransitionState state       = TransitionState.IDLE;
    private long            fadeElapsed = 0L;
    private float           alpha       = 0f;
 
    // self-clocking field for tick()
    private long lastTickNanos = -1L;
 
 
    private static final Screen NULL_SCREEN = new Screen(null) {
        @Override public void update(long elapsedNanos) {}
        @Override public void render(Graphics2D g2)     {}
    };

 
    /**
     * Creates the manager with a no-op sentinel as the active screen.
     * Call {@link #setInitialScreen(Screen)} immediately after constructing
     * the first real Screen.
     */
    public ScreenManager() {
        this.activeScreen = NULL_SCREEN;
    }

 
    /**
     * Replaces the sentinel with the real opening screen — no fade.
     * Intended ONLY for the initial bootstrap in GamePanel.setupGame().
     * After the loop has started, use {@link #transitionTo(Screen)} instead.
     *
     * @param screen must not be null
     */
    public void setInitialScreen(Screen screen) {
        if (screen == null) throw new IllegalArgumentException("screen must not be null");
        this.activeScreen = screen;
        this.activeScreen.onEnter();
    }

 
    /**
     * Requests a fade transition to {@code next}.  Ignored if a transition
     * is already in progress — first caller wins.
     *
     * @param next must not be null
     */
    public void transitionTo(Screen next) {
        if (next == null) throw new IllegalArgumentException("next screen must not be null");
        if (state != TransitionState.IDLE) return;
        pendingScreen = next;
        state         = TransitionState.FADE_OUT;
        fadeElapsed   = 0L;
    }

 
    /**
     * Self-clocking update.  Wired as the {@code updateTick} Runnable in
     * GameLoop — computes elapsed nanos internally then delegates to
     * {@link #update(long)}.
     */
    public void tick() {
        long now = System.nanoTime();
        if (lastTickNanos < 0) {
            lastTickNanos = now;
        }
        long elapsed  = now - lastTickNanos;
        lastTickNanos = now;
        update(elapsed);
    }
 
    /**
     * Advances the active screen and the fade state machine.
     *
     * @param elapsedNanos nanoseconds since the last tick
     */
    public void update(long elapsedNanos) {
        activeScreen.update(elapsedNanos);   // always tick — keeps animations alive
 
        switch (state) {
            case FADE_OUT -> tickFadeOut(elapsedNanos);
            case FADE_IN  -> tickFadeIn(elapsedNanos);
            case IDLE     -> { /* nothing */ }
        }
    }
 
    /**
     * Renders the active screen, then composites the fade overlay on top.
     * Called on the EDT by GamePanel's paintComponent.
     *
     * @param g2 owned by GamePanel — do not dispose
     */
    public void render(Graphics2D g2) {
        activeScreen.render(g2);
 
        if (alpha > 0f) {
            Composite saved = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, ScreenConfig.WIDTH, ScreenConfig.HEIGHT);
            g2.setComposite(saved);
        }
    }
 
 
    private void tickFadeOut(long elapsedNanos) {
        fadeElapsed += elapsedNanos;
        alpha = Math.min(1f, (float) fadeElapsed / FADE_DURATION_NANOS);
 
        if (fadeElapsed >= FADE_DURATION_NANOS) {
            activeScreen.onExit();
            activeScreen  = pendingScreen;
            pendingScreen = null;
            activeScreen.onEnter();
            state       = TransitionState.FADE_IN;
            fadeElapsed = 0L;
        }
    }
 
    private void tickFadeIn(long elapsedNanos) {
        fadeElapsed += elapsedNanos;
        alpha = Math.max(0f, 1f - (float) fadeElapsed / FADE_DURATION_NANOS);
 
        if (fadeElapsed >= FADE_DURATION_NANOS) {
            alpha = 0f;
            state = TransitionState.IDLE;
        }
    }
    
    public Screen getActiveScreen() {
		return activeScreen;
	}



}