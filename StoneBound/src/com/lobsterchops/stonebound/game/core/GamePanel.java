package com.lobsterchops.stonebound.game.core;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.lobsterchops.stonebound.game.config.ColorConfig;
import com.lobsterchops.stonebound.game.config.ScreenConfig;
import com.lobsterchops.stonebound.game.engine.audio.AudioService;
import com.lobsterchops.stonebound.game.engine.audio.content.SoundIds;
import com.lobsterchops.stonebound.game.engine.gfx.RenderLayerKey;
import com.lobsterchops.stonebound.game.engine.gfx.RenderPipeline;
import com.lobsterchops.stonebound.game.engine.input.InputAction;
import com.lobsterchops.stonebound.game.engine.input.InputState;
import com.lobsterchops.stonebound.game.engine.util.DebugMetrics;
import com.lobsterchops.stonebound.game.engine.util.Logger;
import com.lobsterchops.stonebound.game.ui.core.Screen;
import com.lobsterchops.stonebound.game.ui.core.ScreenManager;
import com.lobsterchops.stonebound.game.ui.screens.MenuScreen;
import com.lobsterchops.stonebound.game.ui.screens.SplashScreen;


public class GamePanel extends JPanel implements Runnable {
 
    private static final long serialVersionUID = 1L;
 
    private Thread    gameThread;
    private GameLoop  gameLoop;
 
    private final GameContext gameContext;
 
 
    public GamePanel() {
        this.gameContext = new GameContext();
        initializePanel();
        attachInputListeners();
    }
 
    private void initializePanel() {
        setPreferredSize(new Dimension(ScreenConfig.WIDTH, ScreenConfig.HEIGHT));
        setBackground(ColorConfig.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
    }
 
    private void attachInputListeners() {
        addKeyListener(gameContext.getKeyboard());
        addMouseListener(gameContext.getMouse());
        addMouseMotionListener(gameContext.getMouse());
    }
 
 
    /**
     * Wires the rendering pipeline, sets the initial screen, and builds the
     * game loop.  Must be called before {@link #startGameThread()}.
     */
    public void setupGame() {
        RenderPipeline  pipeline      = ServiceLocator.resolve(RenderPipeline.class);
        ScreenManager   screenManager = ServiceLocator.resolve(ScreenManager.class);
 
        // Register the screen-manager as the UI render layer.
        // This is what makes screens and their fade overlays visible.
        pipeline.addLayer(RenderLayerKey.UI, screenManager::render);
 
        // Set the first real screen — no fade, instant.
        screenManager.setInitialScreen(new SplashScreen(screenManager));
 
        // Build the game loop.
        DebugMetrics metrics = ServiceLocator.resolve(DebugMetrics.class);
        gameLoop = new GameLoop(
            this::update,
            this::repaint,
            metrics
        );
 
        Logger.info("Game setup complete.");
    }
 
 
    public void startGameThread() {
        gameThread = new Thread(this, "GameThread");
        gameThread.setDaemon(true);
        gameThread.start();
        Logger.info("Game thread started.");
    }
 
    public void stopGameThread() {
        if (gameLoop   != null) gameLoop.stop();
        if (gameThread != null) gameThread.interrupt();
        Logger.info("Game thread stopped.");
    }
 
    @Override
    public void run() {
        gameLoop.run();
    }
 
 
    /**
     * One logical tick: poll input → advance screen manager → dispatch
     * screen-specific input → flush input.
     *
     * <p>Note: the render pipeline is NOT driven here.  Rendering happens in
     * {@link #paintComponent(Graphics)}, which Swing calls from the EDT via
     * {@link #repaint()} requested by the game loop.
     */
    private void update() {
        gameContext.updateInput();
 
        ScreenManager screenManager = ServiceLocator.resolve(ScreenManager.class);
        screenManager.tick();
 
        InputState input  = gameContext.getInputManager().poll();
        Screen activeScreen = screenManager.getActiveScreen();
 
        // Route input to whichever screen is active.
        if (activeScreen instanceof MenuScreen menu) {
            menu.handleInput(input);
        }
 
        // Check global actions regardless of active screen.
        if (input.isPressed(InputAction.DEBUG)) {
            // TODO: toggle DebugOverlay when it is wired into the UI layer
        }
 
        gameContext.getInputManager().flush();
    }
 
 
    /**
     * Called by Swing on the EDT.  Hands the Swing {@code Graphics} to the
     * pipeline — the pipeline opens the back buffer, runs all layers, then blits.
     *
     * <p><strong>Do NOT call {@code g.dispose()} here.</strong>  Swing owns
     * this {@code Graphics} instance; disposing it will corrupt the shared
     * graphics context.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Pipeline calls renderer.begin(), runs all layers, then renderer.end(g).
        ServiceLocator.resolve(RenderPipeline.class).render(g);
        // g is NOT disposed — Swing owns it.
    }
}