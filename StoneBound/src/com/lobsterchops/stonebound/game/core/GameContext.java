package com.lobsterchops.stonebound.game.core;

import com.lobsterchops.stonebound.game.engine.audio.AudioCatalog;
import com.lobsterchops.stonebound.game.engine.audio.AudioService;
import com.lobsterchops.stonebound.game.engine.audio.AudioServiceImplementation;
import com.lobsterchops.stonebound.game.engine.audio.content.GameAudio;
import com.lobsterchops.stonebound.game.engine.audio.content.SoundIds;
import com.lobsterchops.stonebound.game.engine.event.EventBus;
import com.lobsterchops.stonebound.game.engine.gfx.Camera;
import com.lobsterchops.stonebound.game.engine.gfx.RenderPipeline;
import com.lobsterchops.stonebound.game.engine.gfx.Renderer;
import com.lobsterchops.stonebound.game.engine.input.InputManager;
import com.lobsterchops.stonebound.game.engine.input.InputState;
import com.lobsterchops.stonebound.game.engine.input.KeyboardInput;
import com.lobsterchops.stonebound.game.engine.input.MouseInput;
import com.lobsterchops.stonebound.game.engine.util.DebugMetrics;
import com.lobsterchops.stonebound.game.engine.util.Logger;
import com.lobsterchops.stonebound.game.gameplay.world.TmxLoader;
import com.lobsterchops.stonebound.game.gameplay.world.World;
import com.lobsterchops.stonebound.game.ui.core.ScreenManager;

public class GameContext {

	private final DebugMetrics debugMetrics;
	private final EventBus eventBus;
	private final Camera camera;
	private final Renderer renderer;
	private final RenderPipeline renderPipeline;
	private final KeyboardInput keyboard;
	private InputState inputState;
	private final MouseInput mouse;
	private final InputManager inputManager;
	private final AudioCatalog audioCatalog;
	private final AudioService audioService;
	private final ScreenManager screenManager;

	public GameContext() {
		Logger.info("Bootstrapping GameContext…");

		debugMetrics = new DebugMetrics();

		eventBus = new EventBus();

		camera = new Camera();
		renderer = new Renderer(camera);
		renderPipeline = new RenderPipeline(renderer);

		keyboard = new KeyboardInput();
		mouse = new MouseInput();
		inputManager = new InputManager(keyboard, mouse);

		audioCatalog = new AudioCatalog();
		GameAudio.registerAll(audioCatalog);
		audioService = new AudioServiceImplementation(audioCatalog);
		screenManager = new ScreenManager();

		ServiceLocator.register(DebugMetrics.class, debugMetrics);
		ServiceLocator.register(EventBus.class, eventBus);
		ServiceLocator.register(Camera.class, camera);
		ServiceLocator.register(Renderer.class, renderer);
		ServiceLocator.register(RenderPipeline.class, renderPipeline);
		ServiceLocator.register(InputManager.class, inputManager);
		ServiceLocator.register(AudioService.class, audioService);
		ServiceLocator.register(AudioCatalog.class, audioCatalog);
		ServiceLocator.register(ScreenManager.class, screenManager);

		Logger.info("GameContext ready.");
		
		// play preloader music
		audioService.play(SoundIds.PRE_LOADER_MUSIC);
		
		
		
	}

	public void setupNewRun() {
		
		Logger.info("Setting up new run…");
		// TODO: load world, spawn player, register HUD layers
		
		World world = new World();
		
		TmxLoader.load("/maps'/world1.tmx", world);

	}

	public void restartRun() {
		Logger.info("Restarting run…");
		setupNewRun();
	}
	
	public void updateInput() {
		inputState = inputManager.poll();
	}

	public DebugMetrics getDebugMetrics() {
		return debugMetrics;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public Camera getCamera() {
		return camera;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public RenderPipeline getRenderPipeline() {
		return renderPipeline;
	}

	public KeyboardInput getKeyboard() {
		return keyboard;
	}

	public MouseInput getMouse() {
		return mouse;
	}

	public InputManager getInputManager() {
		return inputManager;
	}

	public AudioService getAudioService() {
		return audioService;
	}

	public AudioCatalog getAudioCatalog() {
		return audioCatalog;
	}
	
	public ScreenManager getScreenManager() {
		return screenManager;
	}
}