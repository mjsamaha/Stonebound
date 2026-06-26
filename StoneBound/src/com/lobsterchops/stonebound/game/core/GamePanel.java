package com.lobsterchops.stonebound.game.core;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.lobsterchops.stonebound.game.config.ColorConfig;
import com.lobsterchops.stonebound.game.config.ScreenConfig;
import com.lobsterchops.stonebound.game.engine.audio.AudioService;
import com.lobsterchops.stonebound.game.engine.audio.content.SoundIds;
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


    public void setupGame() {
    	
    	ScreenManager screenManager = gameContext.getScreenManager();

        ServiceLocator.resolve(RenderPipeline.class)
                      .addLayer(screenManager::render);

        screenManager.setInitialScreen(new SplashScreen(screenManager));
        
        // play music?
        
        

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
        if (gameLoop  != null) gameLoop.stop();
        if (gameThread != null) gameThread.interrupt();
        Logger.info("Game thread stopped.");
    }


    @Override
    public void run() {
        gameLoop.run();
    }


    private void update() {
    	gameContext.updateInput();

        ScreenManager screenManager = ServiceLocator.resolve(ScreenManager.class);
        screenManager.tick();

        InputState input = gameContext.getInputManager().poll();

        Screen activeScreen = screenManager.getActiveScreen();

        // Dispatch input to whichever screen is active and can handle it.
        if (activeScreen instanceof MenuScreen menu) {
            if (input.isPressed(InputAction.PAUSE)) {
            	ServiceLocator.resolve(AudioService.class).play(SoundIds.UI_CANCEL);
                stopGameThread();
                System.exit(0);
            }
            menu.handleInput(input);
        }

        gameContext.getInputManager().flush();
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        ServiceLocator.resolve(RenderPipeline.class).render(g2);
        g2.dispose();
    }
}